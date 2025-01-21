package la.arya.librarymanagement.controller;

import la.arya.librarymanagement.dto.OrderResponse;
import la.arya.librarymanagement.model.Order;
import la.arya.librarymanagement.repository.IOrderService;
import la.arya.librarymanagement.request.order.AddOrderRequest;
import la.arya.librarymanagement.response.ApiResponse;
import la.arya.librarymanagement.util.Hashid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    @Autowired
    protected  IOrderService orderService;

    @Autowired
    protected Hashid hashIdService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createOrder(@RequestBody AddOrderRequest request) {
        Long userId = hashIdService.decode(request.getUserId());
        OrderResponse response = orderService.placeOrder(userId);
        return ResponseEntity.ok(new ApiResponse("",response));
    }

    @GetMapping("{hashId}")
    public ResponseEntity<ApiResponse> getOrder(@PathVariable String hashId) {
        Long id = hashIdService.decode(hashId);

        Order order = orderService.getOrderById(id);
        OrderResponse response = orderService.convertToOrderResponse(order);
        return ResponseEntity.ok(new ApiResponse("",response));
    }

}
