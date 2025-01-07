package la.arya.librarymanagement.repository;

import la.arya.librarymanagement.model.Image;
import la.arya.librarymanagement.dto.ImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IImageService {
    Image getImageById(Long id);

    Image getImageByName(String name);
    void deleteImageById(Long id);
    List<ImageResponse> saveImages(List<MultipartFile> files, Long product_id) throws IOException;

    void updateImage(MultipartFile file, Long image_id);

}
