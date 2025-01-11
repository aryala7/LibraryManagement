package la.arya.librarymanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryResponse {
    private String hashId;
    private String name;

    @JsonIgnore
    private List<ProductResponse> products;

}
