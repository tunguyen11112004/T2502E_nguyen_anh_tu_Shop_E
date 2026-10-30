package org.fp.shop_e.dto;

import java.util.List;

/**
 * Cấu trúc Phản hồi Lỗi JSON thống nhất theo chuẩn đề bài:
 * {"code": "ERR_STOCK_EMPTY", "message": "Sản phẩm đã hết hàng", "details": [...]}
 */
public record ErrorResponse(
        String code,
        String message,
        List<String> details
) {
}

