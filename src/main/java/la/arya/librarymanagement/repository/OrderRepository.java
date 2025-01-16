package la.arya.librarymanagement.repository;

import la.arya.librarymanagement.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o join fetch o.products where o.user.id =:userId   ")
    List<Order> getAllByUserIdWithProducts(@Param("userId") Long userId);
}
