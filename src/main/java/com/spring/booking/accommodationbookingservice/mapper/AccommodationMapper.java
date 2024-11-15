package com.spring.booking.accommodationbookingservice.mapper;

import com.spring.booking.accommodationbookingservice.config.MapperConfig;
import com.spring.booking.accommodationbookingservice.domain.Accommodation;
import com.spring.booking.accommodationbookingservice.domain.Amenity;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationCreateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationResponse;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationUpdateRequestDto;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface AccommodationMapper {
    @Mapping(source = "country", target = "address.country")
    @Mapping(source = "city", target = "address.city")
    @Mapping(source = "street", target = "address.street")
    @Mapping(source = "postalCode", target = "address.postalCode")
    Accommodation toModel(AccommodationCreateRequestDto createRequestDto);

    @Mapping(source = "country", target = "address.country")
    @Mapping(source = "city", target = "address.city")
    @Mapping(source = "street", target = "address.street")
    @Mapping(source = "postalCode", target = "address.postalCode")
    void toModel(@MappingTarget Accommodation accommodation,
                          AccommodationUpdateRequestDto updateRequestDto);

    AccommodationResponse toResponse(Accommodation accommodation);

    @AfterMapping
    default void setAmenities(@MappingTarget Accommodation accommodation,
                              AccommodationCreateRequestDto createRequestDto) {
        List<Amenity> amenities = createRequestDto.amenities().stream()
                .map(amenityDto -> new Amenity(amenityDto.title(), amenityDto.description()))
                .toList();
        accommodation.setAmenities(amenities);
    }

    @AfterMapping
    default void setAmenities(@MappingTarget Accommodation accommodation,
                              AccommodationUpdateRequestDto updateRequestDto) {
        List<Amenity> amenities = updateRequestDto.amenities().stream()
                .map(amenityDto -> new Amenity(amenityDto.title(), amenityDto.description()))
                .toList();
        accommodation.setAmenities(amenities);
    }
}
