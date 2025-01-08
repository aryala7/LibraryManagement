package la.arya.librarymanagement.request.product;

import la.arya.librarymanagement.model.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequest {
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private int inventory;

    private String brand;

    private Category category;
}
