package com.spring.booking.accommodationbookingservice.repository;

import com.spring.booking.accommodationbookingservice.domain.Accommodation;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    @Query(" select acc from Accommodation acc "
            + " join fetch acc.address "
            + " join fetch acc.amenities ")
    Page<Accommodation> findAllFetchAddressAndAmenities(Pageable pageable);

    @Query(" select acc from Accommodation acc "
            + " join fetch acc.address "
            + " join fetch acc.amenities "
            + " where acc.id = ?1")
    Optional<Accommodation> findByIdFetchAddressAndAmenities(Long accommodationId);
}
