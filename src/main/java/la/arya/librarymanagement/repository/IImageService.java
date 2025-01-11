package la.arya.librarymanagement.repository;

import la.arya.librarymanagement.model.Image;
import la.arya.librarymanagement.dto.ImageResponse;
import la.arya.librarymanagement.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IImageService {
    Image getImageById(Long id);

    Image getImageByName(String name);
    void deleteImageById(Long id);

    ImageResponse uploadImage(MultipartFile file) throws IOException;

    List<ImageResponse> saveImages(List<MultipartFile> files, Product product) throws IOException;

    ImageResponse convertToImageDtoResponse(Image images);

    List<ImageResponse> getConvertedResponse(List<Image> images);

    void updateImage(MultipartFile file, Long image_id);

}
