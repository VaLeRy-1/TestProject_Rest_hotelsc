
import hotelapi.model.Hotel;
import hotelapi.repository.HotelRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class HotelRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HotelRepository hotelRepository;

    @Test
    void searchHotels_WithCityFilter_ShouldReturnMatchingHotels() {
        // Arrange
        Hotel hotel1 = Hotel.builder()
                .name("Hotel Minsk")
                .brand("Brand1")
                .build();
        hotel1.getAddress().setCity("Minsk");

        Hotel hotel2 = Hotel.builder()
                .name("Hotel Moscow")
                .brand("Brand2")
                .build();
        hotel2.getAddress().setCity("Moscow");

        entityManager.persist(hotel1);
        entityManager.persist(hotel2);
        entityManager.flush();

        // Act
        List<Hotel> found = hotelRepository.searchHotels(null, null, "Minsk", null, null);

        // Assert
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getName()).isEqualTo("Hotel Minsk");
    }

    @Test
    void countByCity_ShouldReturnCorrectCounts() {
        // Arrange
        Hotel hotel1 = Hotel.builder().name("Hotel1").build();
        hotel1.getAddress().setCity("Minsk");

        Hotel hotel2 = Hotel.builder().name("Hotel2").build();
        hotel2.getAddress().setCity("Minsk");

        Hotel hotel3 = Hotel.builder().name("Hotel3").build();
        hotel3.getAddress().setCity("Moscow");

        entityManager.persist(hotel1);
        entityManager.persist(hotel2);
        entityManager.persist(hotel3);
        entityManager.flush();

        // Act
        List<Object[]> results = hotelRepository.countByCity();

        // Assert
        assertThat(results).hasSize(2);

        Map<String, Long> counts = new HashMap<>();
        for (Object[] result : results) {
            counts.put((String) result[0], (Long) result[1]);
        }

        assertThat(counts.get("Minsk")).isEqualTo(2L);
        assertThat(counts.get("Moscow")).isEqualTo(1L);
    }
}