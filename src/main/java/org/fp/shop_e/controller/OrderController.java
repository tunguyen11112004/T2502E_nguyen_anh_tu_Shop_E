package org.fp.shop_e.controller;

import org.fp.shop_e.dto.OrderDtos.OrderCreateRequest;
import org.fp.shop_e.dto.OrderDtos.OrderResponse;
import org.fp.shop_e.dto.OrderDtos.OrderStatusUpdateRequest;
import org.fp.shop_e.dto.PageResponse;
import org.fp.shop_e.entity.OrderStatus;
import org.fp.shop_e.openapi.api.OrdersApi;
import org.fp.shop_e.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController implements OrdersApi {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public ResponseEntity<PageResponse<OrderResponse>> getOrders(
            OrderStatus status,
            Long customerId,
            int page,
            int size
    ) {
        return ResponseEntity.ok(orderService.getOrders(status, customerId, page, size));
    }

    @Override
    public ResponseEntity<OrderResponse> getOrderById(Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @Override
    public ResponseEntity<OrderResponse> createOrder(OrderCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    @Override
    public ResponseEntity<OrderResponse> updateOrderStatus(Long id, OrderStatusUpdateRequest request) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, request));
    }
}

