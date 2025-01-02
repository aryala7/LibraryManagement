package la.arya.librarymanagement.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.Date;

@Entity
@Table(name = "loans", uniqueConstraints = @UniqueConstraint(
        columnNames = {
                "user_id",
                "book_id"
        }
))
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @UniqueElements
    private Book book;

    @NotNull
    @PastOrPresent(message = "loan date must be in the past or present")
    private Date loanDate;

    @FutureOrPresent(message = "return date must be in the present or future")
    private Date returnDate;

    @PrePersist
    protected void onCreate() {
        if (this.loanDate == null) {
            this.loanDate = new Date();
        }
    }


}
