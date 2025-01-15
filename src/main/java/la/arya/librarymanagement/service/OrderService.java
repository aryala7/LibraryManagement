package la.arya.librarymanagement.service;

import la.arya.librarymanagement.model.Order;
import la.arya.librarymanagement.repository.IOrderService;
import la.arya.librarymanagement.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    @Override
    public Order placeOrder(Long userId) {
        return null;
    }

    @Override
    public Order getOrderById(Long id) {
        return null;
    }
}
