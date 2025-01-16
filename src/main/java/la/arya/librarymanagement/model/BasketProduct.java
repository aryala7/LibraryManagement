package la.arya.librarymanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "basket_product",indexes = {
        @Index(name = "idx_product_id",columnList = "product_id"),
        @Index(name = "idx_basket_id",columnList = "basket_id")
})
public class BasketProduct {

    @EmbeddedId
    BasketProductKey Id;

    @ManyToOne
    @MapsId("basketId")
    @JoinColumn(name = "basket_id")
    Basket basket;


    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    Product product;

    private BigDecimal price;

    private Integer quantity;

    @Column(name = "created_at",nullable = false,updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at",nullable = false)
    private Timestamp updatedAt;

    @PrePersist
    protected  void onCreate() {
        Timestamp now = Timestamp.from(Instant.now());
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected  void onUpdate() {
        this.updatedAt = Timestamp.from(Instant.now());
    }
}
