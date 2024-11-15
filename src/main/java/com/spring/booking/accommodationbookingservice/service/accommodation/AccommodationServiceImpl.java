package com.spring.booking.accommodationbookingservice.service.accommodation;

import com.spring.booking.accommodationbookingservice.domain.Accommodation;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationCreateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationResponse;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationUpdateRequestDto;
import com.spring.booking.accommodationbookingservice.exception.EntityNotFoundException;
import com.spring.booking.accommodationbookingservice.mapper.AccommodationMapper;
import com.spring.booking.accommodationbookingservice.repository.AccommodationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final AccommodationMapper accommodationMapper;

    @Override
    public AccommodationResponse create(AccommodationCreateRequestDto createRequestDto) {
        Accommodation accommodation = accommodationMapper.toModel(createRequestDto);
        return accommodationMapper.toResponse(accommodationRepository.save(accommodation));
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
    @Transactional
    public void deleteById(Long accommodationId) {
        accommodationRepository.deleteById(accommodationId);
    }
}
