package com.spring.booking.accommodationbookingservice.service.accommodation;

import com.spring.booking.accommodationbookingservice.domain.Accommodation;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationCreateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationResponse;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationUpdateRequestDto;
import com.spring.booking.accommodationbookingservice.mapper.AccommodationMapper;
import com.spring.booking.accommodationbookingservice.repository.AccommodationRepository;
import com.spring.booking.accommodationbookingservice.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AccommodationServiceTest {

    @Mock
    private AccommodationRepository accommodationRepository;

    @InjectMocks
    private AccommodationService accommodationService;

    @Mock
    private AccommodationMapper accommodationMapper;

    private AccommodationCreateRequestDto createRequestDto;
    private AccommodationUpdateRequestDto updateRequestDto;
    private Accommodation accommodation;
    private AccommodationResponse accommodationResponse;
    private PageRequest pageRequest;

    @BeforeEach
    void setUp() {
        createRequestDto = TestUtil.createAccommodationRequestDto();

        updateRequestDto = TestUtil.createUpdateAccommodationRequestDto();

        accommodation = TestUtil.createAccommodation();

        accommodationResponse = TestUtil.createAccommodationResponse();

        pageRequest = TestUtil.createPageRequest();
    }

}