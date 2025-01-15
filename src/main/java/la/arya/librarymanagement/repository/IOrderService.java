package la.arya.librarymanagement.repository;

import la.arya.librarymanagement.model.Order;

public interface IOrderService {

    public Order placeOrder(Long userId);


    public Order getOrderById(Long id);
}
