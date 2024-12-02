package com.spring.booking.accommodationbookingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class AccommodationBookingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccommodationBookingServiceApplication.class, args);
    }

}
