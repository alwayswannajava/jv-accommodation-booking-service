package com.spring.booking.accommodationbookingservice.controller;

import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationCreateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationResponse;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationUpdateRequestDto;
import com.spring.booking.accommodationbookingservice.service.accommodation.AccommodationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accommodations")
@RequiredArgsConstructor
@Tag(name = "Accommodation management",
        description = "Endpoints for managing user's accommodations")
@CacheConfig(cacheNames = "accommodation")
public class AccommodationController {
    private final AccommodationService accommodationService;

    @PostMapping
    @Tag(name = "post", description = "POST methods of Accommodation APIs")
    @Operation(summary = "Create new accommodation", description = "Create new accommodation")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public AccommodationResponse create(@RequestBody @Valid
                                        AccommodationCreateRequestDto createRequestDto) {
        return accommodationService.create(createRequestDto);
    }

    @GetMapping
    @Tag(name = "get", description = "GET methods of Accommodation APIs")
    @Operation(summary = "Get all accommodations", description = "Get all accommodations")
    public Page<AccommodationResponse> findAll(Pageable pageable) {
        return accommodationService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Tag(name = "get", description = "GET methods of Accommodation APIs")
    @Operation(summary = "Get accommodation by ID", description = "Get accommodation by ID")
    @Cacheable(key = "#id")
    public AccommodationResponse findById(@PathVariable Long id) {
        return accommodationService.findById(id);
    }

    @PutMapping("/{id}")
    @Tag(name = "put", description = "PUT methods of Accommodation APIs")
    @Operation(summary = "Update accommodation by ID", description = "Update accommodation by ID")
    @PreAuthorize("hasRole('ADMIN')")
    @CachePut(key = "#id")
    public AccommodationResponse update(@PathVariable Long id,
                                        @RequestBody @Valid
                                        AccommodationUpdateRequestDto updateRequestDto) {
        return accommodationService.update(updateRequestDto, id);
    }

    @DeleteMapping("/{id}")
    @Tag(name = "delete", description = "DELETE methods of Accommodation APIs")
    @Operation(summary = "Delete accommodation by ID", description = "Delete accommodation by ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(key = "#id", beforeInvocation = true)
    public void delete(@PathVariable Long id) {
        accommodationService.deleteById(id);
    }
}
