package com.spring.booking.accommodationbookingservice.domain;

import com.spring.booking.accommodationbookingservice.domain.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.net.URL;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "payments")
@SQLDelete(sql = "UPDATE payments SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Long bookingId;
    @Column(nullable = false, unique = true)
    private URL sessionUrl;
    @Column(nullable = false, unique = true)
    private String sessionId;
    private BigDecimal amountToPay;
    @Column(columnDefinition = "tinyint(1) default 0", nullable = false)
    private boolean isDeleted = false;
}