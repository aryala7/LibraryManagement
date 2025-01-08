package la.arya.librarymanagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "category_sequence")
    @SequenceGenerator(name = "category_sequence", sequenceName = "category_seq", allocationSize = 1)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true )
    private List<Product> products;

    public Category(String name) {
        this.name = name;
    }

}
