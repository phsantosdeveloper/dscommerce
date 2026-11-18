package ph.dev.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ph.dev.dscommerce.entities.OrderItem;
import ph.dev.dscommerce.entities.pk.OrderItemPk;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPk> {
}
