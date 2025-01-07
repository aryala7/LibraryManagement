package la.arya.librarymanagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "books")
public class Book {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @Getter
    @Setter
    private int isbn;

    @Getter
    @Setter
    private int availableCopies;

}
