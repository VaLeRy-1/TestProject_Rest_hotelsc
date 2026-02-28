package hotelapi.service;

import hotelapi.dto.HotelCreateDto;
import hotelapi.dto.HotelDetailDto;
import hotelapi.dto.HotelSummaryDto;

import java.util.List;
import java.util.Map;

public interface HotelService {
    List<HotelSummaryDto> getAllHotels();
    HotelDetailDto getHotelById(Long id);
    List<HotelSummaryDto> searchHotels(String name, String brand, String city, String country, String amenity);
    HotelSummaryDto createHotel(HotelCreateDto hotelCreateDto);
    void addAmenitiesToHotel(Long id, List<String> amenities);
    Map<String, Long> getHistogram(String param);
}