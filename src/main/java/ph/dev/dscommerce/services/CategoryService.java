package ph.dev.dscommerce.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ph.dev.dscommerce.dto.CategoryDTO;
import ph.dev.dscommerce.entities.Category;
import ph.dev.dscommerce.repositories.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> products = categoryRepository.findAll();
        return products.stream()
                .map(category -> new CategoryDTO(category)).toList();
    }

}
