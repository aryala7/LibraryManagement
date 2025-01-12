package la.arya.librarymanagement.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "baskets")
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "basket_sequence")
    @SequenceGenerator(name = "basket_sequence",sequenceName = "basket_seq",allocationSize = 1)
    private Long id;

    private Integer itemCount;

    private BigDecimal totalPrice = BigDecimal.ZERO;

    @ManyToMany
    @JoinTable(
            name = "basket_product",
            joinColumns = @JoinColumn(name = "basket_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products;

    @OneToMany(mappedBy = "basket")
    private Set<BasketProduct> basketProducts;

}
