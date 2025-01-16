package la.arya.librarymanagement.service;

import jakarta.transaction.Transactional;
import la.arya.librarymanagement.enums.OrderStatus;
import la.arya.librarymanagement.model.Basket;
import la.arya.librarymanagement.model.Order;
import la.arya.librarymanagement.model.OrderProduct;
import la.arya.librarymanagement.repository.IBasketService;
import la.arya.librarymanagement.repository.IOrderService;
import la.arya.librarymanagement.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;

    @Autowired
    private final IBasketService basketService;

    @Transactional
    @Override
    public Order placeOrder(Long userId) {

        Basket basket = basketService.getBasketByUserId(userId);
        Order order = createOrder(basket);
        Set<OrderProduct> orderProducts = createOrderItems(basket,order);
        Order savedOrder = orderRepository.save(order);
        basketService.clearBasket(basket.getId());

        return savedOrder;
    }

    @Override
    public Order getOrderById(Long id) {
        return null;
    }


    private Set<OrderProduct> createOrderItems(Basket basket, Order order) {

        basket.getBasketProducts().forEach(p -> {
            order.addProduct(p.getProduct(),p.getPrice(),p.getQuantity());
        });
        return order.getOrderProducts();
    }

    public Order createOrder(Basket basket) {
        Order order = new Order();
        order.setUser(basket.getUser());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(basket.getCreatedAt());
        order.setUpdatedAt(basket.getUpdatedAt());
        return order;
    }

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.getAllByUserIdWithProducts(userId);
    }
}
