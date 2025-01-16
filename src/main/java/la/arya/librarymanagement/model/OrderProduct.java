package la.arya.librarymanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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



    @Column(nullable = false)
    private BigDecimal totalPrice = BigDecimal.valueOf(0);

    @Column(nullable = false)
    private Integer quantity = 0;


    @Column(name = "created_at", nullable = false, updatable = false)
    protected Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    protected Timestamp updatedAt;


    @PrePersist
    public void onCreate() {
        Timestamp now = Timestamp.from(Instant.now());
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

}
