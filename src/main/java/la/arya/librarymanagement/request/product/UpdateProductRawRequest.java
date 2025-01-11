package la.arya.librarymanagement.request.product;

import la.arya.librarymanagement.model.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductRawRequest {
    
    private String name;

    private String description;

    private BigDecimal price;

    private int inventory;

    private String brand;

    private String categoryId;
}
