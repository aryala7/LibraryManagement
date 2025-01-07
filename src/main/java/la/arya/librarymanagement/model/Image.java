package la.arya.librarymanagement.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileType;

    private String downloadUrl;

    private String checksum;

    @ManyToOne
    @JoinColumn(name="product_id",referencedColumnName = "id")
    private Product product;
}
