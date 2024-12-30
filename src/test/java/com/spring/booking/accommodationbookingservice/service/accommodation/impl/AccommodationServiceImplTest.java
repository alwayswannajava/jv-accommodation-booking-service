package com.spring.booking.accommodationbookingservice.service.accommodation.impl;

import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_ACCOMMODATION_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.EXPECTED_ACCOMMODATION_CANCEL_TWICE_MESSAGE;
import static com.spring.booking.accommodationbookingservice.util.Constants.EXPECTED_NOT_FOUND_ACCOMMODATION_ENTITY_MESSAGE;
import static com.spring.booking.accommodationbookingservice.util.Constants.INCORRECT_ACCOMMODATION_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.TELEGRAM_NOTIFICATION_CREATE_ACCOMMODATION_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.spring.booking.accommodationbookingservice.domain.Accommodation;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationCreateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationResponse;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationUpdateRequestDto;
import com.spring.booking.accommodationbookingservice.exception.AccommodationProcessingException;
import com.spring.booking.accommodationbookingservice.exception.EntityNotFoundException;
import com.spring.booking.accommodationbookingservice.mapper.AccommodationMapper;
import com.spring.booking.accommodationbookingservice.repository.AccommodationRepository;
import com.spring.booking.accommodationbookingservice.telegram.NotificationMessageBuilder;
import com.spring.booking.accommodationbookingservice.telegram.TelegramNotificationService;
import com.spring.booking.accommodationbookingservice.util.TestUtil;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class AccommodationServiceImplTest {

    @Mock
    private AccommodationRepository accommodationRepository;

    @InjectMocks
    private AccommodationServiceImpl accommodationService;

    @Mock
    private TelegramNotificationService telegramNotificationService;

    @Mock
    private NotificationMessageBuilder notificationMessageBuilder;

    @Mock
    private AccommodationMapper accommodationMapper;

    private AccommodationCreateRequestDto createRequestDto;
    private AccommodationUpdateRequestDto updateRequestDto;
    private Accommodation accommodation;
    private AccommodationResponse accommodationResponse;
    private AccommodationResponse updatedAccommodationResponse;
    private PageRequest pageRequest;

    @BeforeEach
    void setUp() {
        createRequestDto = TestUtil.createAccommodationRequestDto();

        updateRequestDto = TestUtil.createUpdateAccommodationRequestDto();

        accommodation = TestUtil.createAccommodation();

        accommodationResponse = TestUtil.createAccommodationResponse();

        updatedAccommodationResponse = TestUtil.createUpdateAccommodationResponse();

        pageRequest = TestUtil.createPageRequest();
    }

    @Test
    @DisplayName("Test save() method")
    void save_ValidAccommodationCreateRequestDto_ReturnAccommodationResponse() {
        when(accommodationRepository.save(accommodation)).thenReturn(accommodation);
        when(accommodationMapper.toModel(createRequestDto)).thenReturn(accommodation);
        when(accommodationMapper.toResponse(accommodation)).thenReturn(accommodationResponse);
        when(notificationMessageBuilder.buildNotificationMessage(accommodationResponse))
                .thenReturn(TELEGRAM_NOTIFICATION_CREATE_ACCOMMODATION_MESSAGE);

        AccommodationResponse expected = accommodationResponse;
        AccommodationResponse actual = accommodationService.create(createRequestDto);

        assertEquals(expected, actual);

        verify(accommodationRepository).save(accommodation);
        verify(notificationMessageBuilder).buildNotificationMessage(accommodationResponse);
        verify(telegramNotificationService).sendMessage(notificationMessageBuilder
                .buildNotificationMessage(accommodationResponse));
        verifyNoMoreInteractions(accommodationRepository, accommodationMapper);
    }

    @Test
    @DisplayName("Test findAll() method")
    void findAll_AccommodationExists_ReturnPageOfAccommodationResponse() {
        when(accommodationMapper.toResponse(accommodation)).thenReturn(accommodationResponse);
        when(accommodationRepository.findAllFetchAddressAndAmenities(pageRequest))
                .thenReturn(new PageImpl<>(List.of(accommodation)));

        List<AccommodationResponse> expected = List.of(accommodationResponse);
        Page<AccommodationResponse> actual = accommodationService.findAll(pageRequest);

        assertEquals(expected, actual.getContent());
        assertEquals(expected.size(), actual.getTotalElements());
        verify(accommodationRepository).findAllFetchAddressAndAmenities(pageRequest);
        verifyNoMoreInteractions(accommodationRepository, accommodationMapper);
    }

    @Test
    @DisplayName("Test findById() method")
    void findById_AccommodationExists_ReturnAccommodationResponse() {
        when(accommodationMapper.toResponse(accommodation)).thenReturn(accommodationResponse);
        when(accommodationRepository.findByIdFetchAddressAndAmenities(CORRECT_ACCOMMODATION_ID))
                .thenReturn(Optional.ofNullable(accommodation));

        AccommodationResponse expected = accommodationResponse;
        AccommodationResponse actual = accommodationService.findById(CORRECT_ACCOMMODATION_ID);

        assertEquals(expected, actual);
        verify(accommodationRepository).findByIdFetchAddressAndAmenities(CORRECT_ACCOMMODATION_ID);
        verifyNoMoreInteractions(accommodationRepository);
    }

    @Test
    @DisplayName("Test findById() method by non-existent accommodation")
    void findById_AccommodationNotExists_ThrowEntityNotFoundException() {
        when(accommodationRepository.findByIdFetchAddressAndAmenities(INCORRECT_ACCOMMODATION_ID))
                .thenReturn(Optional.empty());
        EntityNotFoundException entityNotFoundException = assertThrows(
                EntityNotFoundException.class,
                () -> accommodationService.findById(INCORRECT_ACCOMMODATION_ID));

        String actualMessage = entityNotFoundException.getMessage();

        assertEquals(EXPECTED_NOT_FOUND_ACCOMMODATION_ENTITY_MESSAGE, actualMessage);
    }

    @Test
    @DisplayName("Test update() method")
    void update_AccommodationExists_ReturnAccommodationResponse() {
        when(accommodationMapper.toResponse(accommodation))
                .thenReturn(updatedAccommodationResponse);
        when(accommodationRepository.findByIdFetchAddressAndAmenities(CORRECT_ACCOMMODATION_ID))
                .thenReturn(Optional.ofNullable(accommodation));

        AccommodationResponse expected = updatedAccommodationResponse;
        AccommodationResponse actual = accommodationService.update(updateRequestDto,
                CORRECT_ACCOMMODATION_ID);

        assertEquals(expected, actual);
        verify(accommodationRepository).findByIdFetchAddressAndAmenities(CORRECT_ACCOMMODATION_ID);
        verifyNoMoreInteractions(accommodationRepository);
    }

    @Test
    @DisplayName("Test update() method by non-existent accommodation")
    void update_AccommodationNotExists_ThrowEntityNotFoundException() {
        when(accommodationRepository.findByIdFetchAddressAndAmenities(INCORRECT_ACCOMMODATION_ID))
                .thenReturn(Optional.empty());
        EntityNotFoundException entityNotFoundException = assertThrows(
                EntityNotFoundException.class,
                () -> accommodationService.findById(INCORRECT_ACCOMMODATION_ID));

        String actualMessage = entityNotFoundException.getMessage();

        assertEquals(EXPECTED_NOT_FOUND_ACCOMMODATION_ENTITY_MESSAGE, actualMessage);
    }

    @Test
    @DisplayName("Test twice cancellation")
    void deleteById_AccommodationExists_ThrowAccommodationProcessingException() {
        when(accommodationRepository.findByIdFetchAddressAndAmenities(CORRECT_ACCOMMODATION_ID))
                .thenReturn(Optional.ofNullable(accommodation));
        AccommodationProcessingException accommodationProcessingException =
                assertThrows(AccommodationProcessingException.class,
                        () -> {
                            accommodation.setDeleted(true);
                            accommodationService.deleteById(CORRECT_ACCOMMODATION_ID);
                        });

        String actualMessage = accommodationProcessingException.getMessage();

        assertEquals(EXPECTED_ACCOMMODATION_CANCEL_TWICE_MESSAGE, actualMessage);
    }
}
