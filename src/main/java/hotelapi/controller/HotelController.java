package hotelapi.controller;

import hotelapi.dto.HotelCreateDto;
import hotelapi.dto.HotelDetailDto;
import hotelapi.dto.HotelSummaryDto;
import hotelapi.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/property-view")
@RequiredArgsConstructor
@Tag(name = "Hotels", description = "Hotel management endpoints")
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/hotels")
    @Operation(summary = "Get all hotels")
    public ResponseEntity<List<HotelSummaryDto>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @GetMapping("/hotels/{id}")
    @Operation(summary = "Get hotel by ID")
    public ResponseEntity<HotelDetailDto> getHotelById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Search hotels")
    public ResponseEntity<List<HotelSummaryDto>> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String amenities) {
        return ResponseEntity.ok(hotelService.searchHotels(name, brand, city, country, amenities));
    }

    @PostMapping("/hotels")
    @Operation(summary = "Create hotel")
    public ResponseEntity<HotelSummaryDto> createHotel(@Valid @RequestBody HotelCreateDto hotelCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hotelService.createHotel(hotelCreateDto));
    }

    @PostMapping("/hotels/{id}/amenities")
    @Operation(summary = "Add amenities to hotel")
    public ResponseEntity<Void> addAmenitiesToHotel(
            @PathVariable Long id,
            @RequestBody List<String> amenities) {
        hotelService.addAmenitiesToHotel(id, amenities);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/histogram/{param}")
    @Operation(summary = "Get histogram")
    public ResponseEntity<Map<String, Long>> getHistogram(@PathVariable String param) {
        return ResponseEntity.ok(hotelService.getHistogram(param));
    }
}
