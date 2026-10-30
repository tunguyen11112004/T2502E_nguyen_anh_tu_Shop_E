package org.fp.shop_e.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class CategoryDtos {
    private CategoryDtos() {}

    public record CategoryRequest(
            @NotBlank(message = "Tên danh mục không được để trống")
            @Size(max = 120, message = "Tên danh mục tối đa 120 ký tự")
            String name,
            @Size(max = 500, message = "Mô tả tối đa 500 ký tự")
            String description,
            String icon
    ) {}

    public record CategoryResponse(
            Long id,
            String name,
            String slug,
            String description,
            String icon,
            long productCount
    ) {}
}

