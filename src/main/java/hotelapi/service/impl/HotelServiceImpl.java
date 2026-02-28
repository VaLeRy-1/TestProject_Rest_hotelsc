package hotelapi.service.impl;

import hotelapi.dto.HotelCreateDto;
import hotelapi.dto.HotelDetailDto;
import hotelapi.dto.HotelSummaryDto;
import hotelapi.exception.ResourceNotFoundException;
import hotelapi.mapper.HotelMapper;
import hotelapi.model.Amenity;
import hotelapi.model.Hotel;
import hotelapi.repository.AmenityRepository;
import hotelapi.repository.HotelRepository;
import hotelapi.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;
    private final HotelMapper hotelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HotelSummaryDto> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(hotelMapper::toSummaryDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HotelDetailDto getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        return hotelMapper.toDetailDto(hotel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HotelSummaryDto> searchHotels(String name, String brand, String city, String country, String amenity) {
        return hotelRepository.searchHotels(name, brand, city, country, amenity).stream()
                .map(hotelMapper::toSummaryDto)
                .toList();
    }

    @Override
    @Transactional
    public HotelSummaryDto createHotel(HotelCreateDto hotelCreateDto) {
        Hotel hotel = hotelMapper.toEntity(hotelCreateDto);
        hotel = hotelRepository.save(hotel);
        return hotelMapper.toSummaryDto(hotel);
    }

    @Override
    @Transactional
    public void addAmenitiesToHotel(Long id, List<String> amenityNames) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));

        List<Amenity> amenities = amenityNames.stream()
                .map(name -> amenityRepository.findByName(name)
                        .orElseGet(() -> amenityRepository.save(Amenity.builder().name(name).build())))
                .toList();

        hotel.getAmenities().addAll(amenities);
        hotelRepository.save(hotel);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getHistogram(String param) {
        List<Object[]> results = switch (param.toLowerCase()) {
            case "city" -> hotelRepository.countByCity();
            case "country" -> hotelRepository.countByCountry();
            case "brand" -> hotelRepository.countByBrand();
            case "amenities" -> hotelRepository.countByAmenities();
            default -> throw new IllegalArgumentException("Invalid histogram parameter: " + param);
        };

        return results.stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> (Long) arr[1],
                        (v1, v2) -> v1,
                        LinkedHashMap::new
                ));
    }
}