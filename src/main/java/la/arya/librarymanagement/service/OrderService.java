package la.arya.librarymanagement.service;

import jakarta.transaction.Transactional;
import la.arya.librarymanagement.dto.OrderResponse;
import la.arya.librarymanagement.enums.OrderStatus;
import la.arya.librarymanagement.model.Basket;
import la.arya.librarymanagement.model.Order;
import la.arya.librarymanagement.model.OrderProduct;
import la.arya.librarymanagement.repository.IBasketService;
import la.arya.librarymanagement.repository.IOrderService;
import la.arya.librarymanagement.repository.OrderRepository;
import la.arya.librarymanagement.util.Hashid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private final IBasketService basketService;

    @Autowired
    private Hashid hashIdService;

    @Transactional
    @Override
    public OrderResponse placeOrder(Long userId) {
        Basket basket = basketService.getBasketByUserId(userId);
        Order savedOrder = createOrder(basket);
        basketService.clearBasket(basket.getId());
        return convertToOrderResponse(savedOrder);
    }

    @Override
    public Order getOrderById(Long id) {
        return null;
    }

    public Order createOrder(Basket basket) {
        Order order = new Order();
        order.setUser(basket.getUser());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(basket.getCreatedAt());
        order.setUpdatedAt(basket.getUpdatedAt());
        basket.getBasketProducts().forEach(p -> {
            order.addProduct(p.getProduct(),p.getPrice(),p.getQuantity());
        });
        return orderRepository.save(order);
    }

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.getAllByUserIdWithProducts(userId);
    }


    @Override
    public OrderResponse convertToOrderResponse(Order order) {
        OrderResponse response =  modelMapper.map(order, OrderResponse.class);
        response.setHashId(hashIdService.encode(order.getId()));
        return response;
    }

    @Override
    public List<OrderResponse> getConvertedOrders(List<Order> orders) {
        return orders.stream().map(this::convertToOrderResponse).toList();
    }
 }
