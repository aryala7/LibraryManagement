package la.arya.librarymanagement.controller;

import la.arya.librarymanagement.dto.CategoryResponse;
import la.arya.librarymanagement.exception.AlreadyExistsException;
import la.arya.librarymanagement.model.Category;
import la.arya.librarymanagement.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;


    public CategoryController(ICategoryService categoryService, IProductService productService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.hashidService = new Hashid();
        this.categoryRepository = categoryRepository;
    }


    @GetMapping("/index")
    public ResponseEntity<ApiResponse> index() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryResponse> response = categoryService.convertToCategoryResponse(categories,false);
        return ResponseEntity.ok(new ApiResponse("", response));
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
            CategoryResponse response  = categoryService.mapToCategoryResponse(category,includeProducts);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(null, response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Category not found", e.getMessage()));
        }
    }
}
