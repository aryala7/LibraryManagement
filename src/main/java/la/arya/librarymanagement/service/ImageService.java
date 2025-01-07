package la.arya.librarymanagement.service;

import la.arya.librarymanagement.model.Image;
import la.arya.librarymanagement.model.Product;
import la.arya.librarymanagement.dto.ImageResponse;
import la.arya.librarymanagement.excpetion.ResourceNotFoundException;
import la.arya.librarymanagement.repository.IImageService;
import la.arya.librarymanagement.repository.IProductService;
import la.arya.librarymanagement.repository.ImageRepository;
import la.arya.librarymanagement.util.checksumUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    @Value("${file.storage.directory}")
    private String storageDirectory;

    @Autowired
    private final ImageRepository imageRepository;

    private final IProductService productService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("no image found with this id " + id));
    }

    @Override
    public Image getImageByName(String name) {
        return imageRepository.findByFileName(name);
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
            throw new ResourceNotFoundException("no image found with this id " + id);
        });
    }

    @Override
    public List<ImageResponse> saveImages(List<MultipartFile> files, Long product_id) throws IOException {
        Product product = productService.getProductById(product_id);
        List<ImageResponse> savedFiles = new ArrayList<>();

        Files.createDirectories(Paths.get(storageDirectory));

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            String fileName = file.getOriginalFilename();
            String filePath = storageDirectory + File.separator + fileName;

            File outputFile = new File(filePath);
            try(FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String checksum;
            try(InputStream fileInputStream = file.getInputStream()) {
                checksum = checksumUtil.calculateChecksum(fileInputStream,"SHA-256");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Image image = new Image();
            image.setFileName(fileName);
            image.setFileType(file.getContentType());
            image.setDownloadUrl(filePath);
            image.setChecksum(checksum);
            image.setProduct(product);

            Image savedImage = imageRepository.save(image);
            ImageResponse imageDto = new ImageResponse();
            imageDto.setId(savedImage.getId());
            imageDto.setName(savedImage.getFileName());
            imageDto.setDownloadUrl(savedImage.getDownloadUrl());
            savedFiles.add(imageDto);
        }

        return savedFiles;

    }

    @Override
    public void updateImage(MultipartFile file, Long image_id) {
        Image image = getImageById(image_id);
        try {
            image.setFileName(file.getOriginalFilename());
            imageRepository.save(image);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }
}
