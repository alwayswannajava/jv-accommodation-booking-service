package com.spring.booking.accommodationbookingservice.service.payment;

import com.spring.booking.accommodationbookingservice.dto.payment.PaymentResponse;
import com.spring.booking.accommodationbookingservice.mapper.PaymentMapper;
import com.spring.booking.accommodationbookingservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public List<PaymentResponse> findAllByUserId(Long userId) {
        return paymentRepository.findAllByUserId(userId)
                .stream()
                .map(paymentMapper::toPaymentResponse)
                .toList();
    }
}
