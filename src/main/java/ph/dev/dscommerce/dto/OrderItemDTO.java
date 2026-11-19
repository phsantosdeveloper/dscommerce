package ph.dev.dscommerce.dto;

import ph.dev.dscommerce.entities.OrderItem;

public class OrderItemDTO {

    private Long productId;
    private String name;
    private Double price;
    private Integer quantity;
    private String imgUrl;

    public OrderItemDTO() {
    }

    public OrderItemDTO(Long productId, String name, Double price, Integer quantity, String imgUrl) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imgUrl = imgUrl;
    }

    public OrderItemDTO(OrderItem orderItem) {
        productId = orderItem.getProduct().getId();
        name = orderItem.getProduct().getName();
        price = orderItem.getPrice();
        quantity = orderItem.getQuantity();
        imgUrl = orderItem.getProduct().getImgUrl();
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public double getSubTotal() {
        return price * quantity;
    }
}
