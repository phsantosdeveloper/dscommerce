package ph.dev.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ph.dev.dscommerce.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
