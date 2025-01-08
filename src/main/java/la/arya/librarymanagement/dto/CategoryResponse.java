package la.arya.librarymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@AllArgsConstructor
@Data
public class CategoryResponse {
    private String hashId;
    private String name;
    private List<ProductResponse> products;

}
