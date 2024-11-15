package com.spring.booking.accommodationbookingservice.repository;

import com.spring.booking.accommodationbookingservice.domain.Accommodation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    @EntityGraph(attributePaths = {"amenities", "address"})
    Page<Accommodation> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"amenities", "address"})
    Optional<Accommodation> findById(Long accommodationId);
}
