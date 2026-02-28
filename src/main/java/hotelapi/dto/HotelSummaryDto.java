package hotelapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Hotel summary information")
public class HotelSummaryDto {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
}
