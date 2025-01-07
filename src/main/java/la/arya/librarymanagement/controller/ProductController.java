package la.arya.librarymanagement.controller;

import la.arya.librarymanagement.dto.ProductResponse;
import la.arya.librarymanagement.excpetion.ResourceNotFoundException;
import la.arya.librarymanagement.model.Product;
import la.arya.librarymanagement.repository.IProductService;
import la.arya.librarymanagement.request.AddProductRequest;
import la.arya.librarymanagement.request.UpdateProductRequest;
import la.arya.librarymanagement.response.ApiResponse;
import la.arya.librarymanagement.util.Hashid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {

    @Autowired
    private final IProductService productService;

    private final Hashid hashIdService;

    @GetMapping("/index")
    public ResponseEntity<ApiResponse> getAllProducts(
            @RequestParam String hashedCategoryId,
            @RequestParam Optional<String> brand,
            @RequestParam Optional<String> searchKey
    ) {
        Long categoryId = hashIdService.decode(hashedCategoryId);
        List<Product> products = productService.getAllProducts(brand, categoryId,searchKey);
        return ResponseEntity.ok(new ApiResponse("Success",products));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody AddProductRequest product) {

        try {
            Product createdProduct = productService.addProduct(product);
            String hashId = hashIdService.encode(createdProduct.getId());
            ProductResponse response = new ProductResponse(
                    hashId,
                    createdProduct.getName()
            );
            return ResponseEntity.ok(new ApiResponse("Success",response));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/{hash}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable String hash) {

        try {
            Long id = new Hashid().decode(hash);
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(new ApiResponse("Success",product));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }

    }

    @PutMapping("/{hash}")
    public ResponseEntity<ApiResponse> updateProduct(
            @PathVariable String hash,
            @RequestBody UpdateProductRequest product
    ) {

        try {
            Long id = new Hashid().decode(hash);
            Product updateProduct = productService.updateProduct(product,id);
            ProductResponse response = new ProductResponse(
                    hash,
                    updateProduct.getName()
            );
            return ResponseEntity.ok(new ApiResponse("Product Updated!",response));
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
