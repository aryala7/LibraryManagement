package la.arya.librarymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductResponse {

    private String hashId;
    private String name;
    private String description;
    private BigDecimal price;
    private String brand;
    private List<ImageResponse> images;
}
