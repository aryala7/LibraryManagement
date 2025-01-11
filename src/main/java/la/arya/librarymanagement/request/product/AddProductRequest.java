package la.arya.librarymanagement.request.product;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
@Data
public class AddProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private int inventory;
    private String brand;
    private Long categoryId;    // decoded id
    private List<MultipartFile> files;
}