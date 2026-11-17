package ph.dev.dscommerce.dto;

import jakarta.validation.constraints.*;
import ph.dev.dscommerce.entities.Category;
import ph.dev.dscommerce.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDTO {

    private Long id;

    @Size(min = 3, max = 80, message = "Name must be between 3 and 80 characters long")
    @NotBlank(message = "Name is required")
    private String name;

    @Size(min = 10, message = "The description must be at least 10 characters long")
    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    private String imgUrl;

    @NotEmpty(message = "Must have at least one category")
    private List<CategoryDTO> categories = new ArrayList<>();

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, String description, Double price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public ProductDTO(Product product) {
        id = product.getId();
        name = product.getName();
        description = product.getDescription();
        price = product.getPrice();
        imgUrl = product.getImgUrl();
        for(Category cat : product.getCategories()) {
            categories.add(new CategoryDTO(cat));
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }
}
