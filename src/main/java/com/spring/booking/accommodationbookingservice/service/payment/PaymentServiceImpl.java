package com.spring.booking.accommodationbookingservice.service.payment;

import com.spring.booking.accommodationbookingservice.domain.Accommodation;
import com.spring.booking.accommodationbookingservice.domain.Booking;
import com.spring.booking.accommodationbookingservice.domain.Payment;
import com.spring.booking.accommodationbookingservice.domain.enums.Status;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentCancelResponse;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentConfirmResponse;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentCreateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentResponse;
import com.spring.booking.accommodationbookingservice.exception.EntityNotFoundException;
import com.spring.booking.accommodationbookingservice.exception.PaymentProcessingException;
import com.spring.booking.accommodationbookingservice.mapper.PaymentMapper;
import com.spring.booking.accommodationbookingservice.repository.AccommodationRepository;
import com.spring.booking.accommodationbookingservice.repository.BookingRepository;
import com.spring.booking.accommodationbookingservice.repository.PaymentRepository;
import com.spring.booking.accommodationbookingservice.telegram.TelegramNotificationMessageBuilder;
import com.spring.booking.accommodationbookingservice.telegram.TelegramNotificationService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final String PAYMENT_CONFIRM_MESSAGE = "Your payment was "
            + "successfully confirmed.";
    private static final String PAYMENT_CANCEL_MESSAGE = ". You can made it later. "
            + "But keep it in mind, "
            + "that session expires for 24 hours";

    private final AccommodationRepository accommodationRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final BaseStripeClient baseStripeClient;
    private final TelegramNotificationMessageBuilder telegramNotificationMessageBuilder;
    private final TelegramNotificationService telegramNotificationService;

    private Payment payment;

    @Override
    public List<PaymentResponse> findAllByUserId(Long userId) {
        return paymentRepository.findAllByUserId(userId)
                .stream()
                .map(paymentMapper::toPaymentResponse)
                .toList();
    }

    @Override
    @Transactional
    public PaymentResponse create(PaymentCreateRequestDto createRequestDto) throws StripeException {
        Booking booking = bookingRepository.findById(createRequestDto.bookingId())
                .orElseThrow(() -> new EntityNotFoundException("Booking with id: "
                        + createRequestDto.bookingId()
                        + " not found "));
        Accommodation accommodation = checkAccommodationExist(booking.getAccommodationId());
        Session stripeSession = baseStripeClient.buildStripeSession(booking, accommodation);
        payment = paymentMapper.toModel(stripeSession);
        payment.setStatus(Status.UNPAID);
        payment.setBookingId(booking.getId());
        return paymentMapper.toPaymentResponse(paymentRepository.save(payment));
    }

    @Override
    @Transactional
    public PaymentConfirmResponse confirm() throws StripeException {
        baseStripeClient.buildStripeConfirmPayment();
        if (baseStripeClient.isPaymentSuccess()) {
            payment.setStatus(Status.PAID);
            paymentRepository.save(payment);
        }
        PaymentConfirmResponse paymentConfirmResponse = new PaymentConfirmResponse(
                PAYMENT_CONFIRM_MESSAGE,
                payment.getSessionUrl(),
                payment.getSessionId(),
                payment.getStatus(),
                payment.getAmountToPay());
        String builtNotificationMessage = telegramNotificationMessageBuilder
                .buildNotificationMessage(paymentConfirmResponse);
        telegramNotificationService.sendMessage(builtNotificationMessage);
        return paymentConfirmResponse;
    }

    @Override
    @Transactional
    public PaymentCancelResponse cancel() throws StripeException {
        if (baseStripeClient.isPaymentCanceled()) {
            payment.setStatus(Status.CANCELLED);
            paymentRepository.save(payment);
        }
        return new PaymentCancelResponse(PAYMENT_CANCEL_MESSAGE);
    }

    private Accommodation checkAccommodationExist(Long accommodationId) {
        return accommodationRepository
                .findById(accommodationId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new PaymentProcessingException(
                        "Payment can't be created, because "
                        + "accommodation is not present in our booking system. Maybe it was deleted"
                        + " before. We apologize for the inconvenience"));
    }
}
