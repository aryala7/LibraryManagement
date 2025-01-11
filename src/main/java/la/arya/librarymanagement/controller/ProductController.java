package la.arya.librarymanagement.controller;

import jakarta.validation.constraints.NotNull;
import la.arya.librarymanagement.dto.ImageResponse;
import la.arya.librarymanagement.dto.ProductResponse;
import la.arya.librarymanagement.excpetion.ResourceNotFoundException;
import la.arya.librarymanagement.model.Product;
import la.arya.librarymanagement.repository.IImageService;
import la.arya.librarymanagement.repository.IProductService;
import la.arya.librarymanagement.request.product.AddProductRawRequest;
import la.arya.librarymanagement.request.product.AddProductRequest;
import la.arya.librarymanagement.request.product.UpdateProductRawRequest;
import la.arya.librarymanagement.request.product.UpdateProductRequest;
import la.arya.librarymanagement.response.ApiResponse;
import la.arya.librarymanagement.util.Hashid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {

    @Autowired
    private final IProductService productService;

    @Autowired
    private final IImageService imageService;

    private final Hashid hashIdService;

    @GetMapping("/index")
    public ResponseEntity<ApiResponse> getAllProducts(
            @RequestParam @NotNull(message = "categoryId fehlt.") String categoryId,
            @RequestParam Optional<String> brand,
            @RequestParam Optional<String> searchKey
    ) {
        Long decodedCategoryId = hashIdService.decode(categoryId);
        List<Product> products = productService.getAllProducts(brand, decodedCategoryId,searchKey);
        List<ProductResponse> productResponse = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Success",productResponse));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createProduct(@ModelAttribute AddProductRawRequest rawRequest) throws IOException {
        try {
            AddProductRequest request = new AddProductRequest();
            BeanUtils.copyProperties(rawRequest, request);

            Long decodedId = hashIdService.decode(rawRequest.getCategoryId());

            request.setCategoryId(decodedId);
            Product createdProduct = productService.addProduct(request);
            ProductResponse productResponse = productService.convertToDtoResponse(createdProduct);
            productResponse.setHashId(hashIdService.encode(createdProduct.getId()));
            productResponse.getCategory().setHashId(rawRequest.getCategoryId());
            List<ImageResponse> uploadedImages = imageService.saveImages(request.getFiles(),createdProduct);
            productResponse.setImages(uploadedImages);
            return ResponseEntity.ok(new ApiResponse("Success", productResponse));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{hash}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable String hash) {

        try {
            Long id = new Hashid().decode(hash);
            Product product = productService.getProductById(id);
            ProductResponse response = productService.convertToDtoResponse(product);
            return ResponseEntity.ok(new ApiResponse("Success",response));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }

    }

    @PutMapping("/{hash}")
    public ResponseEntity<ApiResponse> updateProduct(
            @PathVariable String hash,
            @RequestBody UpdateProductRawRequest rawRequest
    ) {

        try {
            Long id = hashIdService.decode(hash);
            UpdateProductRequest request = new UpdateProductRequest();
            BeanUtils.copyProperties(rawRequest, request);

            Long decodedId = hashIdService.decode(rawRequest.getCategoryId());

            request.setCategoryId(decodedId);
            Product updateProduct = productService.updateProduct(request,id);
            ProductResponse productResponse = productService.convertToDtoResponse(updateProduct);
            return ResponseEntity.ok(new ApiResponse("Product Updated!",productResponse));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping("/{hash}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable String hash) {

        try{
            Long id = new Hashid().decode(hash);
            productService.deleteProductById(id);
            return ResponseEntity.noContent().build();
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(),null));
        }

    }
}
