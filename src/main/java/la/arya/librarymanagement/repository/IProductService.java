package la.arya.librarymanagement.repository;

import la.arya.librarymanagement.model.Product;
import la.arya.librarymanagement.request.AddProductRequest;
import la.arya.librarymanagement.request.UpdateProductRequest;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    Product addProduct(AddProductRequest request);

    Product getProductById(Long id);

    Product updateProduct(UpdateProductRequest request, Long id);

    void deleteProductById(Long id);

    List<Product> getAllProducts(Optional<String> brand, Long category_id,Optional<String> searchKey);

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByBrand(String brand);

    List<Product> getProductsByCategoryAndBrand(String category, String brand);


    Long CountProductByBrandAndName(String brand, String name);
}