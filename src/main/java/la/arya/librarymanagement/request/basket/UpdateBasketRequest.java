package la.arya.librarymanagement.request.basket;

import lombok.Data;

@Data
public class UpdateBasketRequest {
    private String basketId;
    private String productId;
    private Integer quantity;
}
