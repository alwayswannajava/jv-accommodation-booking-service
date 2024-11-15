package com.spring.booking.accommodationbookingservice.exception;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private int statusCode;
    private LocalDateTime timestamp;
    private List<String> errors;
}
