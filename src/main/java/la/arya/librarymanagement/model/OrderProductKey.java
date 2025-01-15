package la.arya.librarymanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
@Setter
public class OrderProductKey implements Serializable {

    @Column(name = "product_id")
    Long productId;

    @Column(name="order_id")
    Long orderId;
}
