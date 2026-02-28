package hotelapi.mapper;

import hotelapi.dto.*;
import hotelapi.model.Address;
import hotelapi.model.ArrivalTime;
import hotelapi.model.Contacts;
import hotelapi.model.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    @Mapping(target = "address", source = "address", qualifiedByName = "formatAddress")
    @Mapping(target = "phone", source = "contacts.phone")
    HotelSummaryDto toSummaryDto(Hotel hotel);

    HotelDetailDto toDetailDto(Hotel hotel);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    Hotel toEntity(HotelCreateDto dto);

    Address toEntity(AddressDto dto);
    Contacts toEntity(ContactsDto dto);
    ArrivalTime toEntity(ArrivalTimeDto dto);

    AddressDto toDto(Address address);
    ContactsDto toDto(Contacts contacts);
    ArrivalTimeDto toDto(ArrivalTime arrivalTime);

    @Named("formatAddress")
    default String formatAddress(Address address) {
        if (address == null) return null;
        return String.format("%d %s, %s, %s, %s",
                address.getHouseNumber(),
                address.getStreet(),
                address.getCity(),
                address.getPostCode(),
                address.getCountry()
        );
    }

    default List<String> mapAmenities(List<hotelapi.model.Amenity> amenities) {
        return amenities.stream()
                .map(hotelapi.model.Amenity::getName)
                .toList();
    }
}
