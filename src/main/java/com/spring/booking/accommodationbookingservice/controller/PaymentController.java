package com.spring.booking.accommodationbookingservice.controller;

import com.spring.booking.accommodationbookingservice.domain.User;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentCreateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentResponse;
import com.spring.booking.accommodationbookingservice.service.payment.PaymentService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<PaymentResponse> getAllByUserId(@RequestParam(value = "user_id")
                                                    Long userId) {
        return paymentService.findAllByUserId(userId);
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public PaymentResponse createPayment(@RequestBody PaymentCreateRequestDto createRequestDto)
            throws StripeException {
        return paymentService.create(createRequestDto);
    }

    private Long getUserById(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
