package com.spring.booking.accommodationbookingservice.service.booking;

import com.spring.booking.accommodationbookingservice.domain.enums.Status;
import com.spring.booking.accommodationbookingservice.dto.booking.BookingCreateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.booking.BookingResponse;
import com.spring.booking.accommodationbookingservice.dto.booking.BookingUpdateRequestDto;
import java.util.List;

public interface BookingService {
    BookingResponse create(Long userId, BookingCreateRequestDto createRequestDto);

    List<BookingResponse> findByUserIdAndStatus(Long userId, Status status);

    List<BookingResponse> findAllByUserId(Long userId);

    BookingResponse findById(Long bookingId);

    BookingResponse update(Long bookingId, BookingUpdateRequestDto updateRequestDto);

    void deleteById(Long bookingId);
}
