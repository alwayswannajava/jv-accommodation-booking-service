package com.spring.booking.accommodationbookingservice.service.accommodation;

import com.spring.booking.accommodationbookingservice.domain.Accommodation;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationCreateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationResponse;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationUpdateRequestDto;
import com.spring.booking.accommodationbookingservice.exception.AccommodationProcessingException;
import com.spring.booking.accommodationbookingservice.exception.EntityNotFoundException;
import com.spring.booking.accommodationbookingservice.mapper.AccommodationMapper;
import com.spring.booking.accommodationbookingservice.repository.AccommodationRepository;
import com.spring.booking.accommodationbookingservice.telegram.TelegramNotificationMessageBuilder;
import com.spring.booking.accommodationbookingservice.telegram.TelegramNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final AccommodationMapper accommodationMapper;
    private final TelegramNotificationService telegramNotificationService;
    private final TelegramNotificationMessageBuilder telegramNotificationMessageBuilder;

    @Override
    public AccommodationResponse create(AccommodationCreateRequestDto createRequestDto) {
        Accommodation accommodation = accommodationMapper.toModel(createRequestDto);
        AccommodationResponse response = accommodationMapper
                .toResponse(accommodationRepository.save(accommodation));
        String builtNotificationMessage = telegramNotificationMessageBuilder
                .buildNotificationMessage(response);
        telegramNotificationService.sendMessage(builtNotificationMessage);
        return response;
    }

    @Override
    public Page<AccommodationResponse> findAll(Pageable pageable) {
        return accommodationRepository.findAll(pageable)
                .map(accommodationMapper::toResponse);
    }

    @Override
    public AccommodationResponse findById(Long accommodationId) {
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new EntityNotFoundException("Accommodation with id: "
                        + accommodationId
                        + " not found "));
        return accommodationMapper.toResponse(accommodation);
    }

    @Override
    public AccommodationResponse update(AccommodationUpdateRequestDto updateRequestDto,
                                        Long accommodationId) {
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new EntityNotFoundException("Accommodation with id: "
                        + accommodationId
                        + " not found "));
        accommodationMapper.toModel(accommodation, updateRequestDto);
        return accommodationMapper.toResponse(accommodationRepository.save(accommodation));
    }

    @Override
    public void deleteById(Long accommodationId) throws AccommodationProcessingException {
        checkAccommodationCancelTwice(accommodationId);
        accommodationRepository.deleteById(accommodationId);
    }

    private void checkAccommodationCancelTwice(Long accommodationId)
            throws AccommodationProcessingException {
        accommodationRepository.findById(accommodationId)
                .stream()
                .filter(accommodation -> !accommodation.isDeleted())
                .findFirst()
                .orElseThrow(() -> new AccommodationProcessingException("You cannot "
                        + "cancel accommodation with id: "
                        + accommodationId
                        + ", twice"));
    }
}
