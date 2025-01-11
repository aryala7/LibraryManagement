package la.arya.librarymanagement.service;

import la.arya.librarymanagement.model.Image;
import la.arya.librarymanagement.model.Product;
import la.arya.librarymanagement.dto.ImageResponse;
import la.arya.librarymanagement.excpetion.ResourceNotFoundException;
import la.arya.librarymanagement.repository.IImageService;
import la.arya.librarymanagement.repository.ImageRepository;
import la.arya.librarymanagement.util.checksumUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

    @Autowired
    private ModelMapper modelMapper;

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
    public ImageResponse uploadImage(MultipartFile file) throws IOException {
        Files.createDirectories(Paths.get(storageDirectory));
        String fileName = file.getOriginalFilename();
        String filePath = storageDirectory + File.separator + fileName;

        File targetFile = new File(filePath);
        try(FileOutputStream fos = new FileOutputStream(targetFile)) {
            fos.write(file.getBytes());
        }catch (IOException e){
            throw new RuntimeException("failed to write image to file " + filePath);
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

        imageRepository.save(image);

        return convertToImageDtoResponse(image);
    }
    @Override
    public List<ImageResponse> saveImages(List<MultipartFile> files, Product product) throws IOException {
        List<ImageResponse> savedFiles = new ArrayList<>();

        Files.createDirectories(Paths.get(storageDirectory));

        List<Image> images = new ArrayList<>();
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

            images.add(image);
        }

        imageRepository.saveAll(images);
        return getConvertedResponse(images);
    }

    @Override
    public List<ImageResponse> getConvertedResponse(List<Image> images) {
        return images.stream().map(this::convertToImageDtoResponse).toList();
    }


    @Override
    public ImageResponse convertToImageDtoResponse(Image image) {
        return  modelMapper.map(image, ImageResponse.class);
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
