package la.arya.librarymanagement.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "baskets",indexes = {
        @Index(name = "idx_user_id",columnList = "user_Id")
})
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "basket_sequence")
    @SequenceGenerator(name = "basket_sequence",sequenceName = "basket_seq",allocationSize = 1)
    private Long id;

    private Integer itemCount;

    private BigDecimal totalPrice = BigDecimal.ZERO;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "basket_product",
            joinColumns = @JoinColumn(name = "basket_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "basket",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<BasketProduct> basketProducts = new HashSet<>();


    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    @PrePersist
    public void onCreate() {
        Timestamp now = Timestamp.from(Instant.now());
        this.createdAt =  now;
        this.updatedAt = now;
        updateBasketDetails();
    }


    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Timestamp.from(Instant.now());
        updateBasketDetails();
    }


    public void updateBasketDetails() {
        this.itemCount = calculateItemCount();
        this.totalPrice = calculateTotalAmount();
    }

    public void addProduct(Product product, BigDecimal price, Integer quantity) {
        BasketProduct basketProduct = new BasketProduct();

        BasketProductKey key = new BasketProductKey();
        key.setBasketId(this.getId());
        key.setProductId(product.getId());
        basketProduct.setId(key);


        basketProduct.setBasket(this);
        basketProduct.setProduct(product);
        basketProduct.setPrice(price);
        basketProduct.setQuantity(quantity);

        this.basketProducts.add(basketProduct);
    }

    public BigDecimal calculateTotalAmount() {
        return this.basketProducts.stream()
                .map(bp -> bp.getPrice().multiply(new BigDecimal(bp.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public Integer calculateItemCount() {
        return  this.basketProducts.stream().map(BasketProduct::getQuantity).reduce(0, Integer::sum);
    }

    @Column(name = "created_at",nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at",nullable = false)
    private Timestamp updatedAt;


}
