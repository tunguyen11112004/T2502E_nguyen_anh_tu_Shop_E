package org.fp.shop_e.service;

import org.fp.shop_e.dto.OrderDtos.*;
import org.fp.shop_e.dto.PageResponse;
import org.fp.shop_e.entity.*;
import org.fp.shop_e.exception.BusinessException;
import org.fp.shop_e.exception.ResourceNotFoundException;
import org.fp.shop_e.repository.CustomerRepository;
import org.fp.shop_e.repository.OrderRepository;
import org.fp.shop_e.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderService(
            OrderRepository orderRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository
    ) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public PageResponse<OrderResponse> getOrders(OrderStatus status, Long customerId, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1));
        Page<Order> orderPage = orderRepository.searchOrders(status, customerId, pageable);
        return new PageResponse<>(
                orderPage.getContent().stream().map(this::toResponse).toList(),
                orderPage.getNumber(),
                orderPage.getSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages()
        );
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ERR_ORDER_NOT_FOUND", "Không tìm thấy đơn hàng với ID: " + id));
        return toResponse(order);
    }

    public List<OrderResponse> getOrdersByCustomerId(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("ERR_CUSTOMER_NOT_FOUND", "Không tìm thấy khách hàng với ID: " + customerId);
        }
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Phương thức Đặt hàng (Checkout) bọc trong @Transactional để bảo đảm tính toàn vẹn ACID:
     * 1. Kiểm tra tồn kho của toàn bộ sản phẩm trong giỏ
     * 2. Tính tổng tiền
     * 3. Tạo đơn hàng ở trạng thái PENDING
     * 4. Trừ số lượng tồn kho trong CSDL
     */
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "ERR_CUSTOMER_NOT_FOUND",
                        "Không tìm thấy khách hàng với ID: " + request.customerId()
                ));

        Order order = new Order();
        order.setOrderCode(generateOrderCode());
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(
                request.shippingAddress() != null && !request.shippingAddress().isBlank()
                        ? request.shippingAddress().trim()
                        : customer.getAddress()
        );
        order.setNote(request.note());

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<String> stockErrors = new ArrayList<>();

        for (OrderItemRequest itemReq : request.items()) {
            Product product = productRepository.findById(itemReq.productId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "ERR_PRODUCT_NOT_FOUND",
                            "Không tìm thấy sản phẩm với ID: " + itemReq.productId()
                    ));

            if (product.getStockQuantity() < itemReq.quantity()) {
                stockErrors.add(String.format(
                        "Sản phẩm '%s' chỉ còn %d sản phẩm trong kho (yêu cầu: %d)",
                        product.getName(),
                        product.getStockQuantity(),
                        itemReq.quantity()
                ));
            }
        }

        if (!stockErrors.isEmpty()) {
            throw new BusinessException(
                    "ERR_STOCK_EMPTY",
                    "Sản phẩm đã hết hàng",
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    stockErrors
            );
        }

        for (OrderItemRequest itemReq : request.items()) {
            Product product = productRepository.findById(itemReq.productId()).orElseThrow();
            product.setStockQuantity(product.getStockQuantity() - itemReq.quantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem(product, itemReq.quantity());
            order.addItem(orderItem);
            totalAmount = totalAmount.add(orderItem.getSubtotal());
        }

        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);
        return toResponse(savedOrder);
    }

    /**
     * Chuyển đổi trạng thái đơn hàng:
     * PENDING -> PAID -> SHIPPED hoặc CANCELLED.
     * Nếu chuyển sang CANCELLED: hoàn lại số lượng tồn kho cho từng sản phẩm trong đơn.
     */
    @Transactional
    public OrderResponse updateOrderStatus(Long id, OrderStatusUpdateRequest request) {
        Order order = orderRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ERR_ORDER_NOT_FOUND", "Không tìm thấy đơn hàng với ID: " + id));

        OrderStatus currentStatus = order.getStatus();
        OrderStatus targetStatus = request.status();

        if (!currentStatus.canTransitionTo(targetStatus)) {
            throw new BusinessException(
                    "ERR_INVALID_STATUS_TRANSITION",
                    String.format("Không thể chuyển trạng thái đơn hàng từ %s sang %s", currentStatus, targetStatus),
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    List.of("Luồng hợp lệ: PENDING -> PAID -> SHIPPED hoặc PENDING/PAID -> CANCELLED")
            );
        }

        if (targetStatus == OrderStatus.CANCELLED) {
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
                productRepository.save(product);
            }
        }

        order.setStatus(targetStatus);
        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

    private String generateOrderCode() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "ORD-" + datePart + "-" + randomPart;
    }

    public OrderResponse toResponse(Order o) {
        List<OrderItemResponse> itemResponses = o.getItems().stream()
                .map(i -> new OrderItemResponse(
                        i.getId(),
                        i.getProduct() != null ? i.getProduct().getId() : null,
                        i.getProductName(),
                        i.getUnitPrice(),
                        i.getQuantity(),
                        i.getSubtotal()
                ))
                .toList();

        return new OrderResponse(
                o.getId(),
                o.getOrderCode(),
                o.getCustomer() != null ? o.getCustomer().getId() : null,
                o.getCustomer() != null ? o.getCustomer().getFullName() : null,
                o.getCustomer() != null ? o.getCustomer().getEmail() : null,
                o.getCustomer() != null ? o.getCustomer().getPhone() : null,
                o.getStatus(),
                o.getTotalAmount(),
                o.getShippingAddress(),
                o.getNote(),
                itemResponses,
                o.getCreatedAt(),
                o.getUpdatedAt()
        );
    }
}

