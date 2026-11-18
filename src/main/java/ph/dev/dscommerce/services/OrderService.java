package ph.dev.dscommerce.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ph.dev.dscommerce.dto.OrderDTO;
import ph.dev.dscommerce.entities.Order;
import ph.dev.dscommerce.exceptions.ResourceNotFoundException;
import ph.dev.dscommerce.repositories.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = findByOrder(id);
        return new OrderDTO(order);
    }

    @Transactional(readOnly = true)
    private Order findByOrder(Long id) {
        Order searchedOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        return searchedOrder;
    }

}
