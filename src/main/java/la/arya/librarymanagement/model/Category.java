package la.arya.librarymanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
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
