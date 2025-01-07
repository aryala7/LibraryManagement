package la.arya.librarymanagement.request;

import lombok.Data;

@Data
public class AddCategoryRequest {
    private Long id;
    private String name;
    private String slug;
}
