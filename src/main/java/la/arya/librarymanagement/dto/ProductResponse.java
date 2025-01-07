package la.arya.librarymanagement.dto;

import lombok.Data;

@Data
public class ProductResponse {

    private String hashId;
    private String name;

    public ProductResponse(String hashId, String name) {
        this.hashId = hashId;
        this.name = name;
    }
}
