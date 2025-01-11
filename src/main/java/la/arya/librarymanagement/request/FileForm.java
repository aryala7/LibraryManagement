package la.arya.librarymanagement.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileForm {

    private String fileName;
    private MultipartFile file;
}
