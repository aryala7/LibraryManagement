package la.arya.librarymanagement.repository;

import la.arya.librarymanagement.dto.OrderResponse;
import la.arya.librarymanagement.model.Order;

import java.util.List;

public interface IOrderService {

    OrderResponse placeOrder(Long userId);


    Order getOrderById(Long id);

    OrderResponse convertToOrderResponse(Order order);

    List<OrderResponse> getConvertedOrders(List<Order> orders);
}
