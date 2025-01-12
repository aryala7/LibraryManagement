package la.arya.librarymanagement.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class BasketProductResponse {

    private BigDecimal price;

    private Integer quantity;

    private Timestamp updatedAt;

    private String productName;


}
