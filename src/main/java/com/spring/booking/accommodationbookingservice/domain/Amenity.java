package com.spring.booking.accommodationbookingservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "amenities")
@Getter
@Setter
@NoArgsConstructor
public class Amenity extends BaseEntity {
    @Column(nullable = false)
    private String title;
    private String description;

    public Amenity(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
