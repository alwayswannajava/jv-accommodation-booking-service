package com.spring.booking.accommodationbookingservice.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.spring.booking.accommodationbookingservice.domain.Accommodation;
import com.spring.booking.accommodationbookingservice.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase
class AccommodationRepositoryTest {
    @Autowired
    private AccommodationRepository accommodationRepository;

    private PageRequest pageRequest;

    @BeforeEach
    void setUp() {
        pageRequest = TestUtil.createPageRequest();
    }

    @Test
    @DisplayName("Test findAllFetchAddressAndAmenities method()")
    @Sql(scripts = "classpath:db/scripts/add-accommodation-test-data.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/remove-accommodation-test-data.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllFetchAddressAndAmenities_Pageable_ReturnPage() {
        Page<Accommodation> actual = accommodationRepository
                .findAllFetchAddressAndAmenities(pageRequest);
        assertEquals(3, actual.getTotalElements());
    }

    @Test
    @DisplayName("Test findAllFetchAddressAndAmenities method() with empty data")
    void findAllFetchAddressAndAmenities_EmptyData_ReturnEmptyPage() {
        Page<Accommodation> actual = accommodationRepository
                .findAllFetchAddressAndAmenities(pageRequest);
        assertEquals(0, actual.getTotalElements());
    }
}
