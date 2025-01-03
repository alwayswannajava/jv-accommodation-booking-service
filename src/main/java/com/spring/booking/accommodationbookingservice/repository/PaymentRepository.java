package com.spring.booking.accommodationbookingservice.repository;

import com.spring.booking.accommodationbookingservice.domain.Payment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query(" from Payment p "
            + " inner join fetch Booking b "
            + " on p.bookingId = b.id "
            + " where b.userId = ?1 ")
    List<Payment> findAllFetchBookingByUserId(Long userId);

    Optional<Payment> findBySessionId(String sessionId);
}
