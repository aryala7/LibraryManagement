package la.arya.librarymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageResponse {
    private Long id;
    private String name;
    private String downloadUrl;
}
