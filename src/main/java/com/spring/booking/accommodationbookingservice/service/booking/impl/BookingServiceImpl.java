package com.spring.booking.accommodationbookingservice.service.booking.impl;

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
import com.spring.booking.accommodationbookingservice.service.booking.BookingService;
import com.spring.booking.accommodationbookingservice.telegram.NotificationMessageBuilder;
import com.spring.booking.accommodationbookingservice.telegram.TelegramNotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {
    private final AccommodationRepository accommodationRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final TelegramNotificationService telegramNotificationService;
    private final NotificationMessageBuilder notificationMessageBuilder;

    @Override
    public BookingResponse create(Long userId, BookingCreateRequestDto createRequestDto)
            throws BookingProcessingException {
        checkAccommodationExist(createRequestDto.accommodationId());
        checkAccommodationBookingOnTheSameDate(createRequestDto);
        Booking booking = bookingMapper.toModel(createRequestDto);
        booking.setUserId(userId);
        booking.setStatus(Status.PENDING);
        BookingResponse response = bookingMapper.toResponse(bookingRepository.save(booking));
        String builtNotificationMessage = notificationMessageBuilder
                .buildNotificationMessage(response);
        telegramNotificationService.sendMessage(builtNotificationMessage);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public List<BookingResponse> findAllByUserId(Long userId) {
        return bookingRepository.findAllByUserId(userId)
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
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
        return bookingMapper.toResponse(booking);
    }

    @Override
    public void deleteById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                        .orElseThrow(() -> new EntityNotFoundException("Booking with id: "
                                + bookingId
                                + " not found "));
        String builtNotificationMessage = notificationMessageBuilder
                .buildNotificationMessage(booking);
        telegramNotificationService.sendMessage(builtNotificationMessage);
        bookingRepository.deleteById(bookingId);
    }

    private void checkAccommodationExist(Long accommodationId) {
        accommodationRepository.findByIdFetchAddressAndAmenities(accommodationId)
                .orElseThrow(() -> new EntityNotFoundException("Accommodation with id: "
                        + accommodationId
                        + " not found"));
    }

    private void checkAccommodationBookingOnTheSameDate(BookingCreateRequestDto createRequestDto)
            throws BookingProcessingException {

        if (bookingRepository.findByAccommodationIdAndCheckInDateAndCheckOutDate(
                createRequestDto.accommodationId(),
                createRequestDto.checkInDate(),
                createRequestDto.checkOutDate()).isPresent()) {
            throw new BookingProcessingException("Accommodation with id: "
                    + createRequestDto.accommodationId()
                    + " is already booked on this date. Please, choose another date");
        }
    }
}
