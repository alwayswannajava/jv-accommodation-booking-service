package com.spring.booking.accommodationbookingservice.repository;

import com.spring.booking.accommodationbookingservice.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query(" from Payment p "
            + " left join fetch Booking b "
            + " on p.bookingId = b.id "
            + " where b.userId = :userId ")
    List<Payment> findAllByUserId(Long userId);
}
