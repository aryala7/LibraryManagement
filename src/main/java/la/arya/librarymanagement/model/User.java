package la.arya.librarymanagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private  String firstName;
    private  String lastName;

    private  String email;

    private  String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true)
    private Basket basket;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Order> orders;


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
