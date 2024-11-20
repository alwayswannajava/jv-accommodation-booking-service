package com.spring.booking.accommodationbookingservice.service.payment;

import com.spring.booking.accommodationbookingservice.dto.payment.PaymentResponse;
import java.util.List;

public interface PaymentService {
    List<PaymentResponse> findAllByUserId(Long userId);


}
