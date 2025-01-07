package la.arya.librarymanagement.service;

import la.arya.librarymanagement.excpetion.ResourceNotFoundException;
import la.arya.librarymanagement.model.Category;
import la.arya.librarymanagement.model.Product;
import la.arya.librarymanagement.excpetion.ProductNotFoundException;
import la.arya.librarymanagement.repository.CategoryRepository;
import la.arya.librarymanagement.repository.IProductService;
import la.arya.librarymanagement.repository.ProductRepository;
import la.arya.librarymanagement.request.AddProductRequest;
import la.arya.librarymanagement.request.UpdateProductRequest;
import la.arya.librarymanagement.util.Hashid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    private CategoryRepository categoryRepository;

    @Override
    public Product addProduct(AddProductRequest request) {
        // check if the category exists.
        Category category = Optional
                .ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    private Product createProduct(AddProductRequest request,Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product getProductById(Long id) {

        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long id) {
       return productRepository
               .findById(id)
               .map(existingsProduct -> updateExistingProduct(existingsProduct,request))
               .map(productRepository::save)
               .orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));


    }
    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request) {
            existingProduct.setName(request.getName());
            existingProduct.setBrand(request.getBrand());
            existingProduct.setPrice(request.getPrice());
            existingProduct.setInventory(request.getInventory());
            existingProduct.setDescription(request.getDescription());


            Category category = categoryRepository.findByName(request.getCategory().getName());
            existingProduct.setCategory(category);
            return existingProduct;


    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete,() -> {
            throw new ResourceNotFoundException("Product Not Found");
        });
    }

    @Override
    public List<Product> getAllProducts(Optional<String> brand, Long categoryId,Optional<String> searchKey) {

        Specification<Product> specification = Specification.where(null);

        specification = specification.and(((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"), categoryId)));

        if (brand.isPresent()) {
            specification = specification.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("brand"), brand.get()));

        }
        if(searchKey.isPresent()) {
            specification = specification.and(((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%"+searchKey.get()+"%")));
        }
        return productRepository.findAll(specification);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public Long CountProductByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand,name);
    }
}
