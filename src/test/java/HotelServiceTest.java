import hotelapi.dto.HotelCreateDto;
import hotelapi.dto.HotelDetailDto;
import hotelapi.dto.HotelSummaryDto;
import hotelapi.exception.ResourceNotFoundException;
import hotelapi.model.Hotel;
import hotelapi.repository.HotelRepository;
import hotelapi.repository.AmenityRepository;
import hotelapi.mapper.HotelMapper;
import hotelapi.service.impl.HotelServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private AmenityRepository amenityRepository;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private HotelServiceImpl hotelService;

    private Hotel hotel;
    private HotelSummaryDto summaryDto;
    private HotelDetailDto detailDto;

    @BeforeEach
    void setUp() {
        hotel = Hotel.builder()
                .id(1L)
                .name("Test Hotel")
                .brand("Test Brand")
                .build();

        summaryDto = HotelSummaryDto.builder()
                .id(1L)
                .name("Test Hotel")
                .phone("+123456789")
                .address("Test Address")
                .build();

        detailDto = HotelDetailDto.builder()
                .id(1L)
                .name("Test Hotel")
                .brand("Test Brand")
                .build();
    }

    @Test
    void getAllHotels_ShouldReturnListOfHotels() {
        // Arrange
        Mockito.when(hotelRepository.findAll()).thenReturn(Arrays.asList(hotel));
        Mockito.when(hotelMapper.toSummaryDto(ArgumentMatchers.any(Hotel.class))).thenReturn(summaryDto);

        // Act
        List<HotelSummaryDto> result = hotelService.getAllHotels();

        // Assert
        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result.get(0).getName()).isEqualTo("Test Hotel");
        Mockito.verify(hotelRepository).findAll();
        Mockito.verify(hotelMapper, Mockito.times(1)).toSummaryDto(ArgumentMatchers.any(Hotel.class));
    }

    @Test
    void getHotelById_WhenHotelExists_ShouldReturnHotel() {
        // Arrange
        Mockito.when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        Mockito.when(hotelMapper.toDetailDto(ArgumentMatchers.any(Hotel.class))).thenReturn(detailDto);

        // Act
        HotelDetailDto result = hotelService.getHotelById(1L);

        // Assert
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getName()).isEqualTo("Test Hotel");
        Mockito.verify(hotelRepository).findById(1L);
    }

    @Test
    void getHotelById_WhenHotelDoesNotExist_ShouldThrowException() {
        // Arrange
        Mockito.when(hotelRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThatThrownBy(() -> hotelService.getHotelById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Hotel not found with id: 999");
    }

    @Test
    void createHotel_ShouldReturnCreatedHotel() {
        // Arrange
        HotelCreateDto createDto = new HotelCreateDto();
        createDto.setName("New Hotel");

        Mockito.when(hotelMapper.toEntity(ArgumentMatchers.any(HotelCreateDto.class))).thenReturn(hotel);
        Mockito.when(hotelRepository.save(ArgumentMatchers.any(Hotel.class))).thenReturn(hotel);
        Mockito.when(hotelMapper.toSummaryDto(ArgumentMatchers.any(Hotel.class))).thenReturn(summaryDto);

        // Act
        HotelSummaryDto result = hotelService.createHotel(createDto);

        // Assert
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getName()).isEqualTo("Test Hotel");
        Mockito.verify(hotelRepository).save(ArgumentMatchers.any(Hotel.class));
    }

    @Test
    void searchHotels_ShouldReturnFilteredList() {
        // Arrange
        Mockito.when(hotelRepository.searchHotels(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(Arrays.asList(hotel));
        Mockito.when(hotelMapper.toSummaryDto(ArgumentMatchers.any(Hotel.class))).thenReturn(summaryDto);

        // Act
        List<HotelSummaryDto> result = hotelService.searchHotels("Test", null, "Minsk", null, null);

        // Assert
        Assertions.assertThat(result).hasSize(1);
        Mockito.verify(hotelRepository).searchHotels("Test", null, "Minsk", null, null);
    }
}