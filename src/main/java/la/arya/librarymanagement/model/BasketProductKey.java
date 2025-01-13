package la.arya.librarymanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@Setter
@EqualsAndHashCode
public class BasketProductKey implements Serializable {

    @Column(name = "basket_id")
    Long basketId;


    @Column(name = "product_id")
    Long productId;
}
