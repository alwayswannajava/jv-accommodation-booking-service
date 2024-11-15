package com.spring.booking.accommodationbookingservice.repository;

import com.spring.booking.accommodationbookingservice.domain.Accommodation;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    @EntityGraph(attributePaths = {"amenities", "address"})
    Page<Accommodation> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"amenities", "address"})
    Optional<Accommodation> findById(Long accommodationId);
}
