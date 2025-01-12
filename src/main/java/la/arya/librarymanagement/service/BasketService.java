package la.arya.librarymanagement.service;

import la.arya.librarymanagement.dto.BasketResponse;
import la.arya.librarymanagement.excpetion.ResourceNotFoundException;
import la.arya.librarymanagement.model.Basket;
import la.arya.librarymanagement.repository.BasketRepository;
import la.arya.librarymanagement.repository.IBasketService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketService implements IBasketService {

    @Autowired
    private final BasketRepository basketRepository;

    private ModelMapper modelMapper;

    @Override
    public Basket getBasket(Long id) {
        return basketRepository.findById(id).orElse(null);
    }

    @Override
    public Basket createBasket() {
        return null;
    }

    @Override
    public BigDecimal calculateTotalAmount(Long id) {
        return null;
    }

    @Override
    public Basket createBasket(Long id) {
        Basket basket = getBasket(id);
        if (basket == null) {
            throw new ResourceNotFoundException("Basket not found");
        }
        basket.getBasketProducts().clear();
        return basketRepository.save(basket);
    }

    @Override
    public Basket addItemToBasket(Long id, Long itemId) {
        return null;
    }

    @Override
    public Basket removeItemFromBasket(Long id, Long itemId) {
        return null;
    }

    @Override
    public Basket clearBasket(Long id) {
        return null;
    }

    @Override
    public List<BasketResponse> convertToCategoryResponse(List<Basket> baskets) {
        return baskets.stream().map(this::mapToCategoryResponse).toList();
    }
    @Override
    public BasketResponse mapToCategoryResponse(Basket basket) {
        return modelMapper.map(basket, BasketResponse.class);
    }
}
