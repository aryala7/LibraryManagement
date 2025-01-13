package la.arya.librarymanagement.repository;

import la.arya.librarymanagement.dto.BasketResponse;
import la.arya.librarymanagement.model.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface BasketRepository extends JpaRepository<Basket, Long> {

    @Query("select b from Basket b join fetch b.products where b.id = :id")
    Optional<Basket> findByIdWithProducts(@Param("id") Long id);

    @Query("select basket from Basket basket join fetch basket.basketProducts where basket.id = :id")
    Optional<Basket> findByIdWithBasketProducts(@Param("id") Long id);
}
