package la.arya.librarymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BasketResponse {

    private String hashId;

    private Integer itemCount;

    private BigDecimal totalPrice;

    private List<BasketProductResponse>  products;

}
