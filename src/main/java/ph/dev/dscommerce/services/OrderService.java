package ph.dev.dscommerce.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ph.dev.dscommerce.dto.OrderDTO;
import ph.dev.dscommerce.dto.OrderItemDTO;
import ph.dev.dscommerce.entities.Order;
import ph.dev.dscommerce.entities.OrderItem;
import ph.dev.dscommerce.entities.Product;
import ph.dev.dscommerce.entities.User;
import ph.dev.dscommerce.entities.enums.OrderStatus;
import ph.dev.dscommerce.exceptions.ResourceNotFoundException;
import ph.dev.dscommerce.repositories.OrderItemRepository;
import ph.dev.dscommerce.repositories.OrderRepository;
import ph.dev.dscommerce.repositories.ProductRepository;

import java.time.Instant;

@Service
public class OrderService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    public OrderService(OrderItemRepository orderItemRepository, OrderRepository orderRepository, ProductRepository productRepository, UserService userService) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = findByOrder(id);
        return new OrderDTO(order);
    }

    @Transactional
    public OrderDTO insert(OrderDTO dto) {

        Order order = new Order();

        order.setMoment(Instant.now());
        order.setStatus(OrderStatus.WAITING_PAYMENT);

        User user = userService.authenticated();
        order.setClient(user);

        for(OrderItemDTO itemDto : dto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            OrderItem item = new OrderItem(order, product, itemDto.getQuantity(), product.getPrice());

            order.getItems().add(item);
        }

        orderRepository.save(order);
        orderItemRepository.saveAll(order.getItems());

        return new OrderDTO(order);
    }

    @Transactional(readOnly = true)
    private Order findByOrder(Long id) {
        Order searchedOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        return searchedOrder;
    }

}
