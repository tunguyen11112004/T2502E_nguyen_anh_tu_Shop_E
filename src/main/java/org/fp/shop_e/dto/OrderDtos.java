package org.fp.shop_e.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.fp.shop_e.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public final class OrderDtos {
    private OrderDtos() {}

    public record OrderItemRequest(
            @NotNull(message = "Mã sản phẩm là bắt buộc")
            Long productId,
            @NotNull(message = "Số lượng đặt mua là bắt buộc")
            @Min(value = 1, message = "Số lượng đặt mua tối thiểu là 1")
            Integer quantity
    ) {}

    public record OrderCreateRequest(
            @NotNull(message = "Mã khách hàng là bắt buộc")
            Long customerId,
            String shippingAddress,
            String note,
            @NotEmpty(message = "Giỏ hàng không được rỗng khi đặt hàng")
            @Valid
            List<OrderItemRequest> items
    ) {}

    public record OrderStatusUpdateRequest(
            @NotNull(message = "Trạng thái đơn hàng mới là bắt buộc")
            OrderStatus status
    ) {}

    public record OrderItemResponse(
            Long id,
            Long productId,
            String productName,
            BigDecimal unitPrice,
            Integer quantity,
            BigDecimal subtotal
    ) {}

    public record OrderResponse(
            Long id,
            String orderCode,
            Long customerId,
            String customerName,
            String customerEmail,
            String customerPhone,
            OrderStatus status,
            BigDecimal totalAmount,
            String shippingAddress,
            String note,
            List<OrderItemResponse> items,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}
}

