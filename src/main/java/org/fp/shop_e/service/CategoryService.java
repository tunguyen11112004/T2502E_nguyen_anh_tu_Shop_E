package org.fp.shop_e.service;

import org.fp.shop_e.dto.CategoryDtos.CategoryRequest;
import org.fp.shop_e.dto.CategoryDtos.CategoryResponse;
import org.fp.shop_e.entity.Category;
import org.fp.shop_e.exception.BusinessException;
import org.fp.shop_e.exception.ResourceNotFoundException;
import org.fp.shop_e.repository.CategoryRepository;
import org.fp.shop_e.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ERR_CATEGORY_NOT_FOUND", "Không tìm thấy danh mục với ID: " + id));
        return toResponse(category);
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.name().trim())) {
            throw new BusinessException("ERR_DUPLICATE_CATEGORY", "Tên danh mục đã tồn tại", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Category category = new Category(
                request.name().trim(),
                Category.toSlug(request.name()),
                request.description(),
                request.icon() != null && !request.icon().isBlank() ? request.icon() : "tag"
        );
        return toResponse(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ERR_CATEGORY_NOT_FOUND", "Không tìm thấy danh mục với ID: " + id));
        category.setName(request.name().trim());
        category.setDescription(request.description());
        if (request.icon() != null && !request.icon().isBlank()) {
            category.setIcon(request.icon());
        }
        return toResponse(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ERR_CATEGORY_NOT_FOUND", "Không tìm thấy danh mục với ID: " + id));
        long count = productRepository.countByCategoryId(id);
        if (count > 0) {
            throw new BusinessException(
                    "ERR_RESOURCE_IN_USE",
                    "Không thể xóa danh mục đang có sản phẩm",
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    List.of("Danh mục '" + category.getName() + "' đang chứa " + count + " sản phẩm")
            );
        }
        categoryRepository.delete(category);
    }

    private CategoryResponse toResponse(Category c) {
        long count = productRepository.countByCategoryId(c.getId());
        return new CategoryResponse(
                c.getId(),
                c.getName(),
                c.getSlug(),
                c.getDescription(),
                c.getIcon(),
                count
        );
    }
}

