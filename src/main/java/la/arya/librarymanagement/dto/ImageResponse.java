package la.arya.librarymanagement.dto;

import lombok.Data;

@Data
public class ImageResponse {
    private Long id;
    private String name;
    private String downloadUrl;
}
