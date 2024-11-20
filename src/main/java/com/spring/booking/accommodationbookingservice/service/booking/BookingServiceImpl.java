package com.spring.booking.accommodationbookingservice.service.booking;

import com.spring.booking.accommodationbookingservice.domain.Booking;
import com.spring.booking.accommodationbookingservice.domain.enums.Status;
import com.spring.booking.accommodationbookingservice.dto.booking.BookingCreateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.booking.BookingResponse;
import com.spring.booking.accommodationbookingservice.dto.booking.BookingUpdateRequestDto;
import com.spring.booking.accommodationbookingservice.exception.EntityNotFoundException;
import com.spring.booking.accommodationbookingservice.mapper.BookingMapper;
import com.spring.booking.accommodationbookingservice.repository.BookingRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingResponse create(Long userId, BookingCreateRequestDto createRequestDto) {
        Booking booking = bookingMapper.toModel(createRequestDto);
        booking.setUserId(userId);
        booking.setStatus(Status.PENDING);
        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Override
    public List<BookingResponse> findByUserIdAndStatus(Long userId, Status status) {
        if (userId == null) {
            return bookingRepository.findAllByStatus(status)
                    .stream()
                    .map(bookingMapper::toResponse)
                    .toList();
        }
        return bookingRepository.findByUserIdAndStatus(userId, status)
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    public List<BookingResponse> findAllByUserId(Long userId) {
        return bookingRepository.findAllByUserId(userId)
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    public BookingResponse findById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find any bookings with bookingId: "
                + bookingId));
        return bookingMapper.toResponse(booking);
    }

    @Override
    public BookingResponse update(Long bookingId, BookingUpdateRequestDto updateRequestDto) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find any bookings with bookingId: "
                                + bookingId));
        bookingMapper.toModel(booking, updateRequestDto);
        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Override
    public void deleteById(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }
}