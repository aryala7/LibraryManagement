package la.arya.librarymanagement.model;

import jakarta.persistence.*;
import la.arya.librarymanagement.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders",indexes = {
        @Index(name = "idx_user_id",columnList = "user_id"),
})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_sequence")
    @SequenceGenerator(name = "order_sequence",sequenceName = "order_seq",allocationSize = 1)
    private Long id;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name="order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "order",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<OrderProduct> orderProducts = new HashSet<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @Column(name = "created_at", nullable = false, updatable = false)
    protected Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    protected Timestamp updatedAt;


    @PrePersist
    public void onCreate() {
        Timestamp now = Timestamp.from(Instant.now());
        this.createdAt = now;
        this.updatedAt = now;
        this.totalAmount = calculateTotalAmount();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Timestamp.from(Instant.now());
        this.totalAmount = calculateTotalAmount();
    }

    public BigDecimal calculateTotalAmount() {
        return this.orderProducts.stream()
                .map(op-> op.getTotalPrice().multiply(new BigDecimal(op.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public Integer calculateItemCount() {
        return this.orderProducts.stream().map(OrderProduct::getQuantity).reduce(0, Integer::sum);
    }

    public void addProduct(Product product,BigDecimal totalPrice, Integer quantity) {
        OrderProduct orderProduct = new OrderProduct();

        OrderProductKey key = new OrderProductKey();
        key.setProductId(product.getId());
        key.setOrderId(id);

        orderProduct.setId(key);
        orderProduct.setTotalPrice(totalPrice);
        orderProduct.setQuantity(quantity);
        orderProduct.setProduct(product);

        this.orderProducts.add(orderProduct);
    }
}
