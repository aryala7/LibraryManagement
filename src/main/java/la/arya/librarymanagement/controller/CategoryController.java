package la.arya.librarymanagement.controller;

import la.arya.librarymanagement.dto.ProductResponse;
import la.arya.librarymanagement.excpetion.AlreadyExistsException;
import la.arya.librarymanagement.excpetion.ResourceNotFoundException;
import la.arya.librarymanagement.model.Category;
import la.arya.librarymanagement.model.Product;
import la.arya.librarymanagement.repository.ICategoryService;
import la.arya.librarymanagement.repository.IProductService;
import la.arya.librarymanagement.response.ApiResponse;
import la.arya.librarymanagement.service.CategoryService;
import la.arya.librarymanagement.service.ProductService;
import la.arya.librarymanagement.util.Hashid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

    private final ICategoryService categoryService;

    private final IProductService productService;

    private final Hashid hashidService;

    public CategoryController(ICategoryService categoryService, IProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.hashidService = new Hashid();
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createCategory(@RequestBody Category name) {
        try {
            Category createdCategory = categoryService.addCategory(name);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Category created", createdCategory));
        }catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PutMapping("/{hash}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable String hash ,@RequestBody Category category) {
        try {

            Long id = hashidService.decode(hash);
            Category foundedCategory = categoryService.getCategoryById(id);
            if (foundedCategory == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Category not found", category));
            }
            Category createdCategory = categoryService.updateCategory(category,id);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Category created", createdCategory));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/{hash}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable String hash) {
        try {
            Long id = hashidService.decode(hash);
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Category found", category));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Category not found", e));
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<ApiResponse> getCategoryProducts(@PathVariable String name) {

        Category category = categoryService.getCategoryByName(name);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Category not found", category));
        }
        List<Product> products = productService.getAllProducts(null,category.getId(),null);
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product not found", category));
        }
        List<ProductResponse> response = products.stream().map(product ->
                new ProductResponse(
                    hashidService.encode(product.getId()),
                    product.getName())
        ).toList();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(null, response));
    }
}
