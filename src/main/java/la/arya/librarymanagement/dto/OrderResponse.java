package la.arya.librarymanagement.dto;

import la.arya.librarymanagement.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
public class OrderResponse {
    private String hashId;

    private BigDecimal totalPrice;

    private Integer quantity;

    private OrderStatus status;
//
//    private List<ProductResponse> products;
//
//    private UserResponse user;
//
//    private Timestamp updatedAt;


}
