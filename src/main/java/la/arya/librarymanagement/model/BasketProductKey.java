package la.arya.librarymanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class BasketProductKey implements Serializable {

    @Column(name = "basket_id")
    Long basketId;


    @Column(name = "product_id")
    Long productId;
}
