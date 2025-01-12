package la.arya.librarymanagement.repository;

import la.arya.librarymanagement.dto.BasketResponse;
import la.arya.librarymanagement.model.Basket;

import java.math.BigDecimal;
import java.util.List;

public interface IBasketService {
    public Basket getBasket(Long id);

    public Basket createBasket();

    public BigDecimal calculateTotalAmount(Long id);

    public Basket createBasket(Long id);

    public Basket addItemToBasket(Long id, Long itemId);

    public Basket removeItemFromBasket(Long id, Long itemId);

    public Basket clearBasket(Long id);


    BasketResponse mapToCategoryResponse(Basket basket);

    List<BasketResponse> convertToCategoryResponse(List<Basket> baskets);
}
