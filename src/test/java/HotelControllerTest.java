
import hotelapi.controller.HotelController;
import hotelapi.dto.HotelCreateDto;
import hotelapi.dto.HotelDetailDto;
import hotelapi.dto.HotelSummaryDto;
import hotelapi.service.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HotelController.class)
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HotelService hotelService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllHotels_ShouldReturnList() throws Exception {
        // Arrange
        List<HotelSummaryDto> hotels = Arrays.asList(
                HotelSummaryDto.builder().id(1L).name("Hotel 1").build(),
                HotelSummaryDto.builder().id(2L).name("Hotel 2").build()
        );
        when(hotelService.getAllHotels()).thenReturn(hotels);

        // Act & Assert
        mockMvc.perform(get("/property-view/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Hotel 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Hotel 2"));
    }

    @Test
    void getHotelById_ShouldReturnHotel() throws Exception {
        // Arrange
        HotelDetailDto hotel = HotelDetailDto.builder()
                .id(1L)
                .name("Test Hotel")
                .brand("Test Brand")
                .build();
        when(hotelService.getHotelById(1L)).thenReturn(hotel);

        // Act & Assert
        mockMvc.perform(get("/property-view/hotels/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Hotel"))
                .andExpect(jsonPath("$.brand").value("Test Brand"));
    }

    @Test
    void searchHotels_WithParams_ShouldReturnFilteredList() throws Exception {
        // Arrange
        List<HotelSummaryDto> hotels = Arrays.asList(
                HotelSummaryDto.builder().id(1L).name("Minsk Hotel").build()
        );
        when(hotelService.searchHotels(any(), any(), eq("Minsk"), any(), any()))
                .thenReturn(hotels);

        // Act & Assert
        mockMvc.perform(get("/property-view/search")
                        .param("city", "Minsk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Minsk Hotel"));
    }

    @Test
    void createHotel_ShouldReturnCreated() throws Exception {
        // Arrange
        HotelCreateDto createDto = new HotelCreateDto();
        createDto.setName("New Hotel");
        createDto.setBrand("New Brand");

        HotelSummaryDto responseDto = HotelSummaryDto.builder()
                .id(1L)
                .name("New Hotel")
                .build();

        when(hotelService.createHotel(any(HotelCreateDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Hotel"));
    }

    @Test
    void getHistogram_ShouldReturnMap() throws Exception {
        // Arrange
        Map<String, Long> histogram = new HashMap<>();
        histogram.put("Minsk", 5L);
        histogram.put("Moscow", 3L);

        when(hotelService.getHistogram("city")).thenReturn(histogram);

        // Act & Assert
        mockMvc.perform(get("/property-view/histogram/city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Minsk").value(5))
                .andExpect(jsonPath("$.Moscow").value(3));
    }
}
