package ph.dev.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ph.dev.dscommerce.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
