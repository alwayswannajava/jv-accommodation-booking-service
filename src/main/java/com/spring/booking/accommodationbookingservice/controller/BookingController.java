package com.spring.booking.accommodationbookingservice.controller;

import com.spring.booking.accommodationbookingservice.domain.User;
import com.spring.booking.accommodationbookingservice.domain.enums.Status;
import com.spring.booking.accommodationbookingservice.dto.booking.BookingCreateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.booking.BookingResponse;
import com.spring.booking.accommodationbookingservice.dto.booking.BookingUpdateRequestDto;
import com.spring.booking.accommodationbookingservice.service.booking.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings management", description = "Endpoints for managing user's bookings")
@CacheConfig(cacheNames = "booking")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @Tag(name = "post", description = "POST methods of Accommodation APIs")
    @Operation(summary = "Create new booking", description = "Create new booking")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('CUSTOMER')")
    public BookingResponse create(Authentication authentication,
                                  @RequestBody @Valid BookingCreateRequestDto createRequestDto) {
        return bookingService.create(getUserById(authentication), createRequestDto);
    }

    @GetMapping
    @Tag(name = "get", description = "GET methods of Accommodation APIs")
    @Operation(summary = "Get booking by user ID and status",
            description = "Get booking by user ID and status")
    @PreAuthorize("hasRole('ADMIN')")
    public List<BookingResponse> findByUserIdAndStatus(@RequestParam(
            value = "user_id", required = false) Long userId,
                                                       @RequestParam(value = "status")
                                                       Status status) {
        return bookingService.findByUserIdAndStatus(userId, status);
    }

    @GetMapping("/my")
    @Tag(name = "get", description = "GET methods of Accommodation APIs")
    @Operation(summary = "Find all bookings by user ID",
            description = "Find all bookings by user ID")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<BookingResponse> findAllByUserId(Authentication authentication) {
        return bookingService.findAllByUserId(getUserById(authentication));
    }

    @GetMapping("/{booking_id}")
    @Tag(name = "get", description = "GET methods of Accommodation APIs")
    @Operation(summary = "Find booking ID",
            description = "Find booking by ID")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Cacheable(key = "#id")
    public BookingResponse findById(@PathVariable("booking_id") Long bookingId) {
        return bookingService.findById(bookingId);
    }

    @PutMapping("/{booking_id}")
    @Tag(name = "put", description = "PUT methods of Accommodation APIs")
    @Operation(summary = "Update booking by ID",
            description = "Update booking by ID")
    @PreAuthorize("hasRole('CUSTOMER')")
    @CachePut(key = "#bookingId")
    public BookingResponse update(@PathVariable("booking_id") Long bookingId,
                                  @Valid @RequestBody
                                  BookingUpdateRequestDto updateRequestDto) {
        return bookingService.update(bookingId, updateRequestDto);
    }

    @DeleteMapping("/{booking_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Tag(name = "delete", description = "DELETE methods of Accommodation APIs")
    @Operation(summary = "Delete booking by ID",
            description = "Delete booking by ID")
    @PreAuthorize("hasRole('CUSTOMER')")
    @CacheEvict(key = "#bookingId")
    public void deleteById(@PathVariable("booking_id") Long bookingId) {
        bookingService.deleteById(bookingId);
    }

    private Long getUserById(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
