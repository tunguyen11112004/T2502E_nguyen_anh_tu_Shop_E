package org.fp.shop_e.openapi.api;

import jakarta.validation.Valid;
import org.fp.shop_e.dto.PageResponse;
import org.fp.shop_e.dto.ProductDtos.ProductRequest;
import org.fp.shop_e.dto.ProductDtos.ProductResponse;
import org.fp.shop_e.dto.ProductDtos.ProductStockPatchRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * OpenAPI 3.0 Server Interface cho tài nguyên /api/v1/products
 */
@RequestMapping("/api/v1/products")
public interface ProductsApi {

    @GetMapping
    ResponseEntity<PageResponse<ProductResponse>> getProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    );

    @GetMapping("/{id}")
    ResponseEntity<ProductResponse> getProductById(@PathVariable("id") Long id);

    @PostMapping
    ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request);

    @PutMapping("/{id}")
    ResponseEntity<ProductResponse> updateProduct(@PathVariable("id") Long id, @Valid @RequestBody ProductRequest request);

    @PatchMapping("/{id}")
    ResponseEntity<ProductResponse> patchProductStock(@PathVariable("id") Long id, @Valid @RequestBody ProductStockPatchRequest request);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id);
}

