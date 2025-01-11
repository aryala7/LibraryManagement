package la.arya.librarymanagement.request.product;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

// This captures the raw request from the user
@Data
public class AddProductRawRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private int inventory;
    private String brand;
    private String categoryId;
    private List<MultipartFile> files;
}
