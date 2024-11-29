package com.spring.booking.accommodationbookingservice.repository;

import com.spring.booking.accommodationbookingservice.domain.Booking;
import com.spring.booking.accommodationbookingservice.domain.enums.Status;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserIdAndStatus(Long id, Status status);

    List<Booking> findAllByUserId(Long userId);

    List<Booking> findAllByStatus(Status status);

    Optional<Booking> findByAccommodationIdAndCheckInDateAndCheckOutDate(Long accommodationId,
                                                                         LocalDate checkInDate,
                                                                         LocalDate checkOutDate);
}
