package la.arya.librarymanagement.controller;

import la.arya.librarymanagement.dto.ImageResponse;
import la.arya.librarymanagement.excpetion.ResourceNotFoundException;
import la.arya.librarymanagement.model.Image;
import la.arya.librarymanagement.repository.IImageService;
import la.arya.librarymanagement.request.FileForm;
import la.arya.librarymanagement.response.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {

    @Value("${file.storage.directory}")
    private String storageDirectory;
    private final IImageService imageService;
    public ImageController(IImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> handleFileUploadUsingCurl(
            @ModelAttribute FileForm fileForm) throws IOException {
        ImageResponse image = imageService.uploadImage(fileForm.getFile());

        return ResponseEntity.ok(new ApiResponse("Success",image));
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {

        try {
            Path path = Paths.get(storageDirectory).resolve(fileName).normalize();
            Resource resource = new UrlResource(path.toUri());
            System.out.println(resource.exists() + "exists or not");
            System.out.println(resource.isReadable() + "readable or not");
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        }catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }

    }

//    @PostMapping("/upload")
//    public ResponseEntity<ApiResponse> saveImages(List<MultipartFile> files, @RequestParam Long product_id) {
//
//        try {
//            List<ImageResponse> imageDtos = imageService.saveImages(files,product_id);
//            return ResponseEntity.ok(new ApiResponse("Upload Success!", imageDtos));
//        }catch (Exception e){
//            return ResponseEntity
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse("Upload Failed!", e.getMessage()));
//        }
//
//    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName) {
        try {
            Image image = imageService.getImageByName(fileName);
            if (image == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("File not found!", HttpStatus.NOT_FOUND.value()));
            }

            Path filePath = Paths.get(image.getDownloadUrl());
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Image not Found or not Readable", HttpStatus.NOT_FOUND.value()));
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + image.getFileName() + "\"")
                    .contentType(MediaType.parseMediaType(image.getFileType()))
                    .body(resource);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Download Failed!", e.getMessage()));
        }

    }

    @PutMapping("/image/{image_id}")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long image_id, @RequestBody MultipartFile file) {

        try {
            Image image = imageService.getImageById(image_id);
            if (image == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Image not found!", HttpStatus.NOT_FOUND.value()));
            }

            imageService.updateImage(file,image_id);
            return ResponseEntity.ok().body(new ApiResponse("Update Success!", null));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Image not found!", HttpStatus.NOT_FOUND.value()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Update Failed!", e.getMessage()));
        }

    }

    @DeleteMapping("/image/{image_id}")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long image_id) {

        try {
            Image image = imageService.getImageById(image_id);
            if (image == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Image not found!", HttpStatus.NOT_FOUND.value()));
            }

            imageService.deleteImageById(image_id);
            return ResponseEntity.ok().body(new ApiResponse("Delete Success!", null));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Image not found!", HttpStatus.NOT_FOUND.value()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Delete Failed!", e.getMessage()));
        }

    }
}
