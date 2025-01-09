package la.arya.librarymanagement.repository;

import la.arya.librarymanagement.dto.CategoryResponse;
import la.arya.librarymanagement.model.Category;
import la.arya.librarymanagement.request.category.AddCategoryRequest;

import java.util.List;

public interface ICategoryService {

    Category getCategoryById(Long id);

    Category getCategoryByName(String name);
    List<Category> getAllCategories();

    Category addCategory(AddCategoryRequest category);

    Category updateCategory(Category category, Long id);

    void deleteCategoryById(Long id);

    List<CategoryResponse> convertToCategoryResponse(List<Category> categories, boolean includeProducts);

    CategoryResponse mapToCategoryResponse(Category category, boolean includeProducts);
}