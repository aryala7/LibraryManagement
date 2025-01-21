package la.arya.librarymanagement.service;

import la.arya.librarymanagement.dto.CategoryResponse;
import la.arya.librarymanagement.dto.ProductResponse;
import la.arya.librarymanagement.exception.ResourceNotFoundException;
import la.arya.librarymanagement.model.Category;
import la.arya.librarymanagement.model.Product;
import la.arya.librarymanagement.repository.*;
import la.arya.librarymanagement.request.product.AddProductRequest;
import la.arya.librarymanagement.request.product.UpdateProductRequest;
import la.arya.librarymanagement.util.Hashid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private IImageService imageService;

    private final ModelMapper modelmapper;

    private final Hashid hashIdService;

    @Override
    public Product addProduct(AddProductRequest request) throws IOException {
        // check if the category exists.
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category not found"));

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


            Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
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

        specification = specification.and(((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category").get("id"), categoryId)));

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

    @Override
    public List<ProductResponse> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDtoResponse).toList();
    }
    @Override
    public ProductResponse convertToDtoResponse(Product product) {
        ProductResponse productResponse =  modelmapper.map(product, ProductResponse.class);
        productResponse.setHashId(hashIdService.encode(product.getId()));
        CategoryResponse categoryResponse = modelmapper.map(product.getCategory(), CategoryResponse.class);
        categoryResponse.setHashId(hashIdService.encode(product.getCategory().getId()));
        productResponse.setCategory(categoryResponse);
        return productResponse;
    }
}
