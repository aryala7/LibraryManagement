package la.arya.librarymanagement.repository;

import la.arya.librarymanagement.model.Basket;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BasketRepository extends JpaRepository<Basket, Long> {

}
