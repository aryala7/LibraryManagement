package la.arya.librarymanagement.request.basket;

import lombok.Data;

@Data
public class RemoveItemRequest {
    private String basketId;
    private String productId;
}
