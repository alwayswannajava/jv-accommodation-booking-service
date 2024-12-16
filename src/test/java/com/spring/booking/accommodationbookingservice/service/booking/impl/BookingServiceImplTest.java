package com.spring.booking.accommodationbookingservice.service.booking.impl;

import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_ACCOMMODATION_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_BOOKING_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_USER_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.EXPECTED_NOT_FOUND_BOOKING_ENTITY_MESSAGE;
import static com.spring.booking.accommodationbookingservice.util.Constants.INCORRECT_BOOKING_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.TELEGRAM_NOTIFICATION_CREATE_BOOKING_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.spring.booking.accommodationbookingservice.domain.Accommodation;
import com.spring.booking.accommodationbookingservice.domain.Booking;
import com.spring.booking.accommodationbookingservice.domain.enums.Status;
import com.spring.booking.accommodationbookingservice.dto.booking.BookingCreateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.booking.BookingResponse;
import com.spring.booking.accommodationbookingservice.dto.booking.BookingUpdateRequestDto;
import com.spring.booking.accommodationbookingservice.exception.BookingProcessingException;
import com.spring.booking.accommodationbookingservice.exception.EntityNotFoundException;
import com.spring.booking.accommodationbookingservice.mapper.BookingMapper;
import com.spring.booking.accommodationbookingservice.repository.AccommodationRepository;
import com.spring.booking.accommodationbookingservice.repository.BookingRepository;
import com.spring.booking.accommodationbookingservice.telegram.TelegramNotificationMessageBuilder;
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

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private AccommodationRepository accommodationRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private TelegramNotificationService telegramNotificationService;

    @Mock
    private TelegramNotificationMessageBuilder telegramNotificationMessageBuilder;

    @Mock
    private BookingMapper bookingMapper;
    private Accommodation accommodation;
    private BookingCreateRequestDto createRequestDto;
    private BookingUpdateRequestDto updateRequestDto;
    private Booking booking;
    private BookingResponse bookingResponse;
    private BookingResponse updatedBookingResponse;

    @BeforeEach
    void setUp() {
        accommodation = TestUtil.createAccommodation();

        createRequestDto = TestUtil.createBookingRequestDto();

        updateRequestDto = TestUtil.createBookingUpdateRequestDto();

        booking = TestUtil.createBooking();

        bookingResponse = TestUtil.createBookingResponse();

        updatedBookingResponse = TestUtil.createUpdateBookingResponse();
    }

    @Test
    @DisplayName("Test save() method")
    void save_ValidBookingCreateRequestDto_ReturnBookingResponse()
            throws BookingProcessingException {
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.toModel(createRequestDto)).thenReturn(booking);
        when(bookingMapper.toResponse(booking)).thenReturn(bookingResponse);
        when(accommodationRepository.findByIdFetchAddressAndAmenities(CORRECT_ACCOMMODATION_ID))
                .thenReturn(Optional.ofNullable(accommodation));
        when(telegramNotificationMessageBuilder.buildNotificationMessage(bookingResponse))
                .thenReturn(TELEGRAM_NOTIFICATION_CREATE_BOOKING_MESSAGE);

        BookingResponse expected = bookingResponse;
        BookingResponse actual = bookingService.create(CORRECT_USER_ID, createRequestDto);

        assertEquals(expected, actual);

        verify(bookingRepository).save(booking);
        verify(telegramNotificationMessageBuilder).buildNotificationMessage(bookingResponse);
        verify(telegramNotificationService).sendMessage(telegramNotificationMessageBuilder
                .buildNotificationMessage(bookingResponse));
    }

    @Test
    @DisplayName("Test findByUserIdAndStatus() method")
    void findByUserIdAndStatus_BookingExists_ReturnListOfBookingResponse() {
        when(bookingMapper.toResponse(booking)).thenReturn(bookingResponse);
        when(bookingRepository.findByUserIdAndStatus(CORRECT_USER_ID, Status.PENDING))
                .thenReturn(List.of(booking));

        List<BookingResponse> expected = List.of(bookingResponse);
        List<BookingResponse> actual = bookingService.findByUserIdAndStatus(CORRECT_USER_ID,
                Status.PENDING);

        assertEquals(expected, actual);
        assertEquals(expected.size(), actual.size());
        verify(bookingRepository).findByUserIdAndStatus(CORRECT_USER_ID,
                Status.PENDING);
        verifyNoMoreInteractions(bookingRepository, bookingMapper);
    }

    @Test
    @DisplayName("Test findAllByUserId() method")
    void findAll_BookingExists_ReturnListOfBookingResponse() {
        when(bookingMapper.toResponse(booking)).thenReturn(bookingResponse);
        when(bookingRepository.findAllByUserId(CORRECT_USER_ID))
                .thenReturn(List.of(booking));

        List<BookingResponse> expected = List.of(bookingResponse);
        List<BookingResponse> actual = bookingService.findAllByUserId(CORRECT_USER_ID);

        assertEquals(expected, actual);
        assertEquals(expected.size(), actual.size());
        verify(bookingRepository).findAllByUserId(CORRECT_USER_ID);
        verifyNoMoreInteractions(bookingRepository, bookingMapper);
    }

    @Test
    @DisplayName("Test findById() method")
    void findById_BookingExists_ReturnBookingResponse() {
        when(bookingMapper.toResponse(booking)).thenReturn(bookingResponse);
        when(bookingRepository.findById(CORRECT_BOOKING_ID))
                .thenReturn(Optional.ofNullable(booking));

        BookingResponse expected = bookingResponse;
        BookingResponse actual = bookingService.findById(CORRECT_BOOKING_ID);

        assertEquals(expected, actual);
        verify(bookingRepository).findById(CORRECT_BOOKING_ID);
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    @DisplayName("Test findById() method by non-existent booking")
    void findById_BookingNotExists_ThrowEntityNotFoundException() {
        when(bookingRepository.findById(INCORRECT_BOOKING_ID))
                .thenReturn(Optional.empty());
        EntityNotFoundException entityNotFoundException = assertThrows(
                EntityNotFoundException.class,
                () -> bookingService.findById(INCORRECT_BOOKING_ID));

        String actualMessage = entityNotFoundException.getMessage();

        assertEquals(EXPECTED_NOT_FOUND_BOOKING_ENTITY_MESSAGE, actualMessage);
    }

    @Test
    @DisplayName("Test update() method")
    void update_BookingExists_ReturnBookingResponse() {
        when(bookingMapper.toResponse(booking)).thenReturn(updatedBookingResponse);
        when(bookingRepository.findById(CORRECT_BOOKING_ID))
                .thenReturn(Optional.ofNullable(booking));

        BookingResponse expected = updatedBookingResponse;
        BookingResponse actual = bookingService.update(CORRECT_BOOKING_ID,
                updateRequestDto);

        assertEquals(expected, actual);
        verify(bookingRepository).findById(CORRECT_BOOKING_ID);
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    @DisplayName("Test update() method by non-existent booking")
    void update_BookingNotExists_ThrowEntityNotFoundException() {
        when(bookingRepository.findById(INCORRECT_BOOKING_ID))
                .thenReturn(Optional.empty());
        EntityNotFoundException entityNotFoundException = assertThrows(
                EntityNotFoundException.class,
                () -> bookingService.findById(INCORRECT_BOOKING_ID));

        String actualMessage = entityNotFoundException.getMessage();

        assertEquals(EXPECTED_NOT_FOUND_BOOKING_ENTITY_MESSAGE, actualMessage);
    }
}
