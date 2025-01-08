package la.arya.librarymanagement.service;

import la.arya.librarymanagement.dto.CategoryResponse;
import la.arya.librarymanagement.dto.ProductResponse;
import la.arya.librarymanagement.model.Category;
import la.arya.librarymanagement.excpetion.AlreadyExistsException;
import la.arya.librarymanagement.excpetion.ResourceNotFoundException;
import la.arya.librarymanagement.repository.CategoryRepository;
import la.arya.librarymanagement.repository.ICategoryService;
import la.arya.librarymanagement.request.category.AddCategoryRequest;
import la.arya.librarymanagement.util.Hashid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {


    @Autowired
    private CategoryRepository categoryRepository;

    private final Hashid hashIdService;

    public CategoryService() {
         this.hashIdService = new Hashid();
    }


    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(AddCategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new AlreadyExistsException(request.getName() + " Already exists!");
        }
        Category category = new Category(request.getName());
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id)).map(oldCategory -> {
                    oldCategory.setName(category.getName());
                    return categoryRepository.save(oldCategory);
                }).orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository
                .findById(id)
                .ifPresentOrElse(categoryRepository::delete, () -> {
                    throw new ResourceNotFoundException("Category not found!");
                });
    }

}
