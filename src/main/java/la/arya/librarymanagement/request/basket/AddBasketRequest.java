package la.arya.librarymanagement.request.basket;

import lombok.Data;

@Data
public class AddBasketRequest {
    private Long productId;
    private Integer quantity;

}
