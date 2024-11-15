package com.spring.booking.accommodationbookingservice.service.accommodation;

import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationCreateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationResponse;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccommodationService {
    AccommodationResponse create(AccommodationCreateRequestDto createRequestDto);

    Page<AccommodationResponse> findAll(Pageable pageable);

    AccommodationResponse findById(Long accommodationId);

    AccommodationResponse update(AccommodationUpdateRequestDto updateRequestDto, Long accommodationId);

    void deleteById(Long accommodationId);
}
