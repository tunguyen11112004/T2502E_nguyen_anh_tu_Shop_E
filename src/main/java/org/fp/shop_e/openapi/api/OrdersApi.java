package org.fp.shop_e.openapi.api;

import jakarta.validation.Valid;
import org.fp.shop_e.dto.OrderDtos.OrderCreateRequest;
import org.fp.shop_e.dto.OrderDtos.OrderResponse;
import org.fp.shop_e.dto.OrderDtos.OrderStatusUpdateRequest;
import org.fp.shop_e.dto.PageResponse;
import org.fp.shop_e.entity.OrderStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * OpenAPI 3.0 Server Interface cho tài nguyên /api/v1/orders
 */
@RequestMapping("/api/v1/orders")
public interface OrdersApi {

    @GetMapping
    ResponseEntity<PageResponse<OrderResponse>> getOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );

    @GetMapping("/{id}")
    ResponseEntity<OrderResponse> getOrderById(@PathVariable("id") Long id);

    @PostMapping
    ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request);

    @PatchMapping("/{id}")
    ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable("id") Long id, @Valid @RequestBody OrderStatusUpdateRequest request);
}

