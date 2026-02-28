package hotelapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelCreateDto {
    @NotBlank(message = "Hotel name is required")
    private String name;

    private String description;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotNull(message = "Address is required")
    private AddressDto address;

    @NotNull(message = "Contacts are required")
    private ContactsDto contacts;

    @NotNull(message = "Arrival time is required")
    private ArrivalTimeDto arrivalTime;
}
