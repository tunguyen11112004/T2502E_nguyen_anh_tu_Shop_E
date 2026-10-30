package org.fp.shop_e.controller;

import org.fp.shop_e.dto.PageResponse;
import org.fp.shop_e.dto.ProductDtos.ProductRequest;
import org.fp.shop_e.dto.ProductDtos.ProductResponse;
import org.fp.shop_e.dto.ProductDtos.ProductStockPatchRequest;
import org.fp.shop_e.openapi.api.ProductsApi;
import org.fp.shop_e.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class ProductController implements ProductsApi {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public ResponseEntity<PageResponse<ProductResponse>> getProducts(
            String keyword,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {
        return ResponseEntity.ok(productService.searchProducts(
                keyword, categoryId, minPrice, maxPrice, page, size, sortBy, sortDir
        ));
    }

    @Override
    public ResponseEntity<ProductResponse> getProductById(Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Override
    public ResponseEntity<ProductResponse> createProduct(ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @Override
    public ResponseEntity<ProductResponse> updateProduct(Long id, ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @Override
    public ResponseEntity<ProductResponse> patchProductStock(Long id, ProductStockPatchRequest request) {
        return ResponseEntity.ok(productService.patchProductStock(id, request));
    }

    @Override
    public ResponseEntity<Void> deleteProduct(Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}

