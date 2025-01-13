package la.arya.librarymanagement.service;

import la.arya.librarymanagement.dto.BasketProductResponse;
import la.arya.librarymanagement.dto.BasketResponse;
import la.arya.librarymanagement.excpetion.ResourceNotFoundException;
import la.arya.librarymanagement.model.Basket;
import la.arya.librarymanagement.model.BasketProduct;
import la.arya.librarymanagement.model.Product;
import la.arya.librarymanagement.repository.BasketRepository;
import la.arya.librarymanagement.repository.IBasketService;
import la.arya.librarymanagement.repository.IProductService;
import la.arya.librarymanagement.request.basket.AddBasketRequest;
import la.arya.librarymanagement.util.Hashid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BasketService implements IBasketService {

    @Autowired
    private final BasketRepository basketRepository;

    @Autowired
    private final IProductService productService;


    @Autowired
    private final ModelMapper modelMapper;

    private final Hashid hashIdService;

    @Override
    public Basket getBasket(Long id) {
        return basketRepository.findById(id).orElse(null);
    }


    @Override
    public BasketResponse createBasket(AddBasketRequest request) {

        Product product = productService.getProductById(request.getProductId());

        Basket basket = new Basket();
        basket.addProduct(product,product.getPrice(),request.getQuantity());
        basket.setTotalPrice(basket.calculateTotalAmount());
        basket.setItemCount(basket.calculateItemCount());
        return mapToBasketResponse(basketRepository.save(basket));
    }

    @Override
    public BigDecimal calculateTotalAmount(Set<BasketProduct> basketProducts) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (BasketProduct basketProduct : basketProducts) {
            totalAmount = totalAmount.add(basketProduct.getPrice().multiply(new BigDecimal(basketProduct.getQuantity())));
        }
        return totalAmount;
    }


    @Override
    public Basket addItemToBasket(Long basketId, Long itemId) {

        Basket basket = basketRepository.findById(basketId).orElseThrow( () -> new ResourceNotFoundException("Basket not found"));
        Product product = productService.getProductById(itemId);
//
//        Set<Product> products = basket.getProducts();
//        products.add(product);
//        basketRepository.save(basket);
//        Basket updatedBasket = basket.setTotalPrice(calculateTotalAmount(products));
        return basket;
    }

    @Override
    public Basket removeItemFromBasket(Long id, Long itemId) {
        return null;
    }

    @Override
    public Basket clearBasket(Long id) {
        Basket basket = getBasket(id);
        if (basket == null) {
            throw new ResourceNotFoundException("Basket not found");
        }
        basket.getBasketProducts().clear();
        return basketRepository.save(basket);
    }

    @Override
    public List<BasketResponse> convertToBasketResponse(List<Basket> baskets) {
        return baskets.stream().map(this::mapToBasketResponse).toList();
    }
    @Override
    public BasketResponse mapToBasketResponse(Basket basket) {
        BasketResponse response = modelMapper.map(basket, BasketResponse.class);
        List<BasketProductResponse> basketProductResponses = this.convertToBasketProductResponse(basket.getBasketProducts());

        response.setHashId(hashIdService.encode(basket.getId()));
        response.setProducts(basketProductResponses);
        return response;
    }

    @Override
    public BasketProductResponse mapToBasketProductResponse(BasketProduct basketProduct) {
       return modelMapper.map(basketProduct, BasketProductResponse.class);
    }

    @Override
    public List<BasketProductResponse> convertToBasketProductResponse(Set<BasketProduct> basketProducts) {
        return basketProducts.stream().map(this::mapToBasketProductResponse).toList();
    }
}
