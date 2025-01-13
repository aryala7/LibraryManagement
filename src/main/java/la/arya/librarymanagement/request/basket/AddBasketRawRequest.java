package la.arya.librarymanagement.request.basket;

import lombok.Data;

@Data
public class AddBasketRawRequest {
    private String productHashId;
    private Integer quantity;

}
