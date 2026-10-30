package org.fp.shop_e.service;

import org.fp.shop_e.dto.PageResponse;
import org.fp.shop_e.dto.ProductDtos.ProductRequest;
import org.fp.shop_e.dto.ProductDtos.ProductResponse;
import org.fp.shop_e.dto.ProductDtos.ProductStockPatchRequest;
import org.fp.shop_e.entity.Category;
import org.fp.shop_e.entity.Product;
import org.fp.shop_e.exception.ResourceNotFoundException;
import org.fp.shop_e.repository.CategoryRepository;
import org.fp.shop_e.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public PageResponse<ProductResponse> searchProducts(
            String keyword,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {
        String validSortField = switch (sortBy != null ? sortBy : "createdAt") {
            case "price", "name", "stockQuantity", "id" -> sortBy;
            default -> "createdAt";
        };
        Sort sort = "asc".equalsIgnoreCase(sortDir)
                ? Sort.by(validSortField).ascending()
                : Sort.by(validSortField).descending();
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), sort);

        Page<Product> productPage = productRepository.searchProducts(keyword, categoryId, minPrice, maxPrice, pageable);
        return new PageResponse<>(
                productPage.getContent().stream().map(this::toResponse).toList(),
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages()
        );
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findWithCategoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ERR_PRODUCT_NOT_FOUND", "Không tìm thấy sản phẩm với ID: " + id));
        return toResponse(product);
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("ERR_CATEGORY_NOT_FOUND", "Không tìm thấy danh mục với ID: " + request.categoryId()));

        String sku = (request.sku() != null && !request.sku().isBlank())
                ? request.sku().trim().toUpperCase()
                : "SKU-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        String imageUrl = (request.imageUrl() != null && !request.imageUrl().isBlank())
                ? request.imageUrl().trim()
                : "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800";

        Product product = new Product(
                sku,
                request.name().trim(),
                request.description(),
                request.price(),
                imageUrl,
                request.stockQuantity(),
                category
        );
        return toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findWithCategoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ERR_PRODUCT_NOT_FOUND", "Không tìm thấy sản phẩm với ID: " + id));
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("ERR_CATEGORY_NOT_FOUND", "Không tìm thấy danh mục với ID: " + request.categoryId()));

        if (request.sku() != null && !request.sku().isBlank()) {
            product.setSku(request.sku().trim().toUpperCase());
        }
        product.setName(request.name().trim());
        product.setDescription(request.description());
        product.setPrice(request.price());
        if (request.imageUrl() != null && !request.imageUrl().isBlank()) {
            product.setImageUrl(request.imageUrl().trim());
        }
        product.setStockQuantity(request.stockQuantity());
        product.setCategory(category);
        return toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse patchProductStock(Long id, ProductStockPatchRequest request) {
        Product product = productRepository.findWithCategoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ERR_PRODUCT_NOT_FOUND", "Không tìm thấy sản phẩm với ID: " + id));
        if (request.stockQuantity() != null) {
            product.setStockQuantity(request.stockQuantity());
        }
        if (request.price() != null) {
            product.setPrice(request.price());
        }
        return toResponse(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ERR_PRODUCT_NOT_FOUND", "Không tìm thấy sản phẩm với ID: " + id));
        product.setActive(false);
        productRepository.save(product);
    }

    public ProductResponse toResponse(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getSku(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getImageUrl(),
                p.getStockQuantity(),
                p.getCategory() != null ? p.getCategory().getId() : null,
                p.getCategory() != null ? p.getCategory().getName() : null,
                p.getCreatedAt()
        );
    }
}

