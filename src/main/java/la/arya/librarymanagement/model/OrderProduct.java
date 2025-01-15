package la.arya.librarymanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="order_product",indexes = {
        @Index(name = "idx_order_id",columnList = "order_id"),
        @Index(name = "idx_product_id", columnList = "product_id")
})
public class OrderProduct {
    @EmbeddedId
    OrderProductKey Id;


    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    public Order order;


    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    public Product product;


    private Timestamp createdAt;

    private Timestamp updatedAt;

}
