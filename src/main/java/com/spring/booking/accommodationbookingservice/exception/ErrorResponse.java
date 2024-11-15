package com.spring.booking.accommodationbookingservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private int statusCode;
    private LocalDateTime timestamp;
    private List<String> errors;
}
