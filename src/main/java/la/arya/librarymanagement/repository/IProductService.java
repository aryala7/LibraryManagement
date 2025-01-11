package la.arya.librarymanagement.repository;

import la.arya.librarymanagement.dto.ProductResponse;
import la.arya.librarymanagement.model.Product;
import la.arya.librarymanagement.request.product.AddProductRequest;
import la.arya.librarymanagement.request.product.UpdateProductRequest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IProductService {
    Product addProduct(AddProductRequest request) throws IOException;

    Product getProductById(Long id);

    Product updateProduct(UpdateProductRequest request, Long id);

    void deleteProductById(Long id);

    List<Product> getAllProducts(Optional<String> brand, Long category_id,Optional<String> searchKey);

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByBrand(String brand);

    List<Product> getProductsByCategoryAndBrand(String category, String brand);


    Long CountProductByBrandAndName(String brand, String name);

    List<ProductResponse> getConvertedProducts(List<Product> products);

    ProductResponse convertToDtoResponse(Product product);
}