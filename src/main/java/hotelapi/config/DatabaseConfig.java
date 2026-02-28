package hotelapi.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("com.example.hotelapi.model")
@EnableJpaRepositories("com.example.hotelapi.repository")
public class DatabaseConfig {
    // Конфигурация для JPA
}