package la.arya.librarymanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Set;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products",indexes = {
        @Index(name = "idx_category_id",columnList = "category_id")
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private String description;

    private BigDecimal price;

    private int inventory;

    private String brand;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;


    @ManyToMany(mappedBy = "products")
    private Set<Order> orders;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Image> images;


    public Product(String name, String description, BigDecimal price, int inventory, String brand, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.inventory = inventory;
        this.brand = brand;
        this.category = category;
    }

    @ManyToMany(mappedBy = "products")
    private Set<Basket> baskets;


    @OneToMany(mappedBy = "product")
    private Set<BasketProduct> basketProducts;


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
