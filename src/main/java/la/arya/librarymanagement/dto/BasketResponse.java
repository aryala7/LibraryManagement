package la.arya.librarymanagement.dto;

import lombok.Data;

import java.math.BigDecimal;

import java.util.List;

@Data
public class BasketResponse {

    private Integer itemCount;

    private BigDecimal totalPrice;

    private List<BasketProductResponse>  products;

}
