package com.spring.booking.accommodationbookingservice.service.accommodation.impl;

import com.spring.booking.accommodationbookingservice.domain.Accommodation;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationCreateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationResponse;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationUpdateRequestDto;
import com.spring.booking.accommodationbookingservice.exception.AccommodationProcessingException;
import com.spring.booking.accommodationbookingservice.exception.EntityNotFoundException;
import com.spring.booking.accommodationbookingservice.mapper.AccommodationMapper;
import com.spring.booking.accommodationbookingservice.repository.AccommodationRepository;
import com.spring.booking.accommodationbookingservice.service.accommodation.AccommodationService;
import com.spring.booking.accommodationbookingservice.telegram.NotificationMessageBuilder;
import com.spring.booking.accommodationbookingservice.telegram.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccommodationServiceImpl implements AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final AccommodationMapper accommodationMapper;
    private final NotificationService notificationService;
    private final NotificationMessageBuilder notificationMessageBuilder;

    @Override
    public AccommodationResponse create(AccommodationCreateRequestDto createRequestDto) {
        Accommodation accommodation = accommodationMapper.toModel(createRequestDto);
        AccommodationResponse response = accommodationMapper
                .toResponse(accommodationRepository.save(accommodation));
        String builtNotificationMessage = notificationMessageBuilder
                .buildNotificationMessage(response);
        notificationService.sendMessage(builtNotificationMessage);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccommodationResponse> findAll(Pageable pageable) {
        return accommodationRepository.findAllFetchAddressAndAmenities(pageable)
                .map(accommodationMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public AccommodationResponse findById(Long accommodationId) {
        Accommodation accommodation = accommodationRepository.findByIdFetchAddressAndAmenities(
                accommodationId)
                .orElseThrow(() -> new EntityNotFoundException("Accommodation with id: "
                        + accommodationId
                        + " not found "));
        return accommodationMapper.toResponse(accommodation);
    }

    @Override
    public AccommodationResponse update(AccommodationUpdateRequestDto updateRequestDto,
                                        Long accommodationId) {
        Accommodation accommodation = accommodationRepository.findByIdFetchAddressAndAmenities(
                accommodationId)
                .orElseThrow(() -> new EntityNotFoundException("Accommodation with id: "
                        + accommodationId
                        + " not found "));
        accommodationMapper.toModel(accommodation, updateRequestDto);
        return accommodationMapper.toResponse(accommodation);
    }

    @Override
    public void deleteById(Long accommodationId) throws AccommodationProcessingException {
        checkAccommodationCancelTwice(accommodationId);
        accommodationRepository.deleteById(accommodationId);
    }

    private void checkAccommodationCancelTwice(Long accommodationId)
            throws AccommodationProcessingException {
        accommodationRepository.findByIdFetchAddressAndAmenities(accommodationId)
                .stream()
                .filter(accommodation -> !accommodation.isDeleted())
                .findFirst()
                .orElseThrow(() -> new AccommodationProcessingException("Cannot cancel "
                        + "accommodation: "
                        + accommodationId
                        + " twice"));
    }
}
