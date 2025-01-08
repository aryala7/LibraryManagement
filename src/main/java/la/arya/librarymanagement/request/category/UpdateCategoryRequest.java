package la.arya.librarymanagement.request.category;

import lombok.Data;

@Data
public class UpdateCategoryRequest {
    private String hashId;
    private String name;
}
