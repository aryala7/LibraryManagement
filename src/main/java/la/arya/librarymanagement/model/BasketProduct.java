package la.arya.librarymanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "basket_product")
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

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
