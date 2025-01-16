package la.arya.librarymanagement.repository;

import la.arya.librarymanagement.dto.BasketProductResponse;
import la.arya.librarymanagement.dto.BasketResponse;
import la.arya.librarymanagement.model.Basket;
import la.arya.librarymanagement.model.BasketProduct;
import la.arya.librarymanagement.request.basket.AddBasketRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface IBasketService {
    public Basket getBasket(Long id);

    public BigDecimal calculateTotalAmount(Set<BasketProduct> basketProducts);

    public BasketResponse createBasket(AddBasketRequest request);

    public BasketResponse addItemToBasket(Long id, Long itemId,Integer quantity);

    public BasketResponse removeItemFromBasket(Long id, Long itemId);

    public Basket clearBasket(Long id);


    BasketResponse mapToBasketResponse(Basket basket);

    List<BasketResponse> convertToBasketResponse(List<Basket> baskets);

    BasketProductResponse mapToBasketProductResponse(BasketProduct basketProduct);

    List<BasketProductResponse> convertToBasketProductResponse(Set<BasketProduct> basketProducts);

    Basket getBasketByUserId(Long userId);
}
