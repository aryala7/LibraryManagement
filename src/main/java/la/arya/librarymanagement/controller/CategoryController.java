package la.arya.librarymanagement.controller;

import la.arya.librarymanagement.dto.CategoryResponse;
import la.arya.librarymanagement.dto.ProductResponse;
import la.arya.librarymanagement.excpetion.AlreadyExistsException;
import la.arya.librarymanagement.excpetion.ResourceNotFoundException;
import la.arya.librarymanagement.model.Category;
import la.arya.librarymanagement.model.Product;
import la.arya.librarymanagement.repository.ICategoryService;
import la.arya.librarymanagement.repository.IProductService;
import la.arya.librarymanagement.request.category.AddCategoryRequest;
import la.arya.librarymanagement.request.category.UpdateCategoryRequest;
import la.arya.librarymanagement.response.ApiResponse;
import la.arya.librarymanagement.util.Hashid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
    public ResponseEntity<ApiResponse> createCategory(@RequestBody AddCategoryRequest name) {
        try {
            Category createdCategory = categoryService.addCategory(name);
            String hashId = hashidService.encode(createdCategory.getId());
            CategoryResponse response = new CategoryResponse(
                    hashId,
                    createdCategory.getName(),
                    null
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Category created", response));
        }catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PutMapping("/{hash}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable String hash ,@RequestBody UpdateCategoryRequest category) {
        try {
            Long id = hashidService.decode(hash);
            Category foundedCategory = categoryService.getCategoryById(id);
            if (foundedCategory == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Category not found", category));
            }
            foundedCategory.setName(category.getName());
            Category updatedCategory = categoryService.updateCategory(foundedCategory,id);
            CategoryResponse response = new CategoryResponse(
                    hash,
                    updatedCategory.getName(),
                    null
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Category updated", response));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/{hash}")
    public ResponseEntity<ApiResponse> getCategoryById(
            @PathVariable String hash,
            @RequestParam(required = false)Set<String> includes
            ) {

        boolean includeProducts = includes != null &&
                includes.stream().anyMatch(i -> i.equalsIgnoreCase("products"));
        try {
            Long id = hashidService.decode(hash);
            Category category = categoryService.getCategoryById(id);
            CategoryResponse response = mapToCategoryResponse(category, includeProducts);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(null, response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Category not found", e.getMessage()));
        }
    }

    private CategoryResponse mapToCategoryResponse(Category category,boolean includeProducts) {
        List<ProductResponse> products = includeProducts ?
                category.getProducts().stream()
                        .map(product -> new ProductResponse(
                                hashidService.encode(product.getId()),
                                product.getName()
                        ))
                        .toList()
                : null;
        return new CategoryResponse(
                this.hashidService.encode(category.getId()),
                category.getName(),
                products
        );
    }
    @GetMapping("/{name}/products")
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
