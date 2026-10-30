package org.fp.shop_e.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class ProductDtos {
    private ProductDtos() {}

    public record ProductRequest(
            String sku,
            @NotBlank(message = "Tên sản phẩm không được để trống")
            String name,
            String description,
            @NotNull(message = "Giá bán không được để trống")
            @DecimalMin(value = "0.01", message = "Giá bán phải lớn hơn 0")
            BigDecimal price,
            String imageUrl,
            @NotNull(message = "Số lượng tồn kho không được để trống")
            @Min(value = 0, message = "Số lượng tồn kho phải lớn hơn hoặc bằng 0")
            Integer stockQuantity,
            @NotNull(message = "Danh mục sản phẩm là bắt buộc")
            Long categoryId
    ) {}

    public record ProductStockPatchRequest(
            @Min(value = 0, message = "Số lượng tồn kho không được âm")
            Integer stockQuantity,
            @DecimalMin(value = "0.01", message = "Giá bán phải lớn hơn 0")
            BigDecimal price
    ) {}

    public record ProductResponse(
            Long id,
            String sku,
            String name,
            String description,
            BigDecimal price,
            String imageUrl,
            Integer stockQuantity,
            Long categoryId,
            String categoryName,
            LocalDateTime createdAt
    ) {}
}

