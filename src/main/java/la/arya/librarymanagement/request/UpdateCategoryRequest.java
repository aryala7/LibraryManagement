package la.arya.librarymanagement.request;

import lombok.Data;

@Data
public class UpdateCategoryRequest {
    private Long id;
    private String name;
    private String slug;
}
