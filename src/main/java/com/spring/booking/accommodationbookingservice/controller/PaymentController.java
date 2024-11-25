package com.spring.booking.accommodationbookingservice.controller;

import com.spring.booking.accommodationbookingservice.dto.payment.PaymentCancelResponse;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentConfirmResponse;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentCreateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentResponse;
import com.spring.booking.accommodationbookingservice.service.payment.PaymentService;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payments management", description = "Endpoints for managing user's payments")
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
    @Tag(name = "post", description = "POST methods of Accommodation APIs")
    @Operation(summary = "Create new payment", description = "Create new payment")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse createPayment(@RequestBody PaymentCreateRequestDto createRequestDto)
            throws StripeException {
        return paymentService.create(createRequestDto);
    }

    @GetMapping("/success")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Tag(name = "get", description = "GET methods of Accommodation APIs")
    @Operation(summary = "Success payment", description = "Create new payment")
    public PaymentConfirmResponse success() throws StripeException {
        return paymentService.confirm();
    }

    @GetMapping("/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Tag(name = "get", description = "GET methods of Accommodation APIs")
    @Operation(summary = "Cancel payment", description = "Cancel payment")
    public PaymentCancelResponse cancel() throws StripeException {
        return paymentService.cancel();
    }
}
