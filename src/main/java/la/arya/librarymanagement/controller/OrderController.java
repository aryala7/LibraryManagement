package la.arya.librarymanagement.controller;

import la.arya.librarymanagement.dto.OrderResponse;
import la.arya.librarymanagement.model.Order;
import la.arya.librarymanagement.repository.IOrderService;
import la.arya.librarymanagement.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    @Autowired
    protected  IOrderService orderService;


    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createOrder(Long userId) {
        OrderResponse response = orderService.placeOrder(userId);
        return ResponseEntity.ok(new ApiResponse("",response));
    }

}
