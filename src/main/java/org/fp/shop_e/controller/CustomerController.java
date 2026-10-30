package org.fp.shop_e.controller;

import org.fp.shop_e.dto.CustomerDtos.CustomerCreateRequest;
import org.fp.shop_e.dto.CustomerDtos.CustomerLoginRequest;
import org.fp.shop_e.dto.CustomerDtos.CustomerResponse;
import org.fp.shop_e.dto.CustomerDtos.CustomerUpdateRequest;
import org.fp.shop_e.dto.OrderDtos.OrderResponse;
import org.fp.shop_e.openapi.api.CustomersApi;
import org.fp.shop_e.service.CustomerService;
import org.fp.shop_e.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController implements CustomersApi {

    private final CustomerService customerService;
    private final OrderService orderService;

    public CustomerController(CustomerService customerService, OrderService orderService) {
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @Override
    public ResponseEntity<List<CustomerResponse>> getCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @Override
    public ResponseEntity<CustomerResponse> getCustomerById(Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @Override
    public ResponseEntity<CustomerResponse> registerCustomer(CustomerCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.registerCustomer(request));
    }

    @Override
    public ResponseEntity<CustomerResponse> createCustomerSession(CustomerLoginRequest request) {
        return ResponseEntity.ok(customerService.authenticate(request));
    }

    @Override
    public ResponseEntity<CustomerResponse> updateCustomer(Long id, CustomerUpdateRequest request) {
        return ResponseEntity.ok(customerService.updateCustomer(id, request));
    }

    @Override
    public ResponseEntity<List<OrderResponse>> getCustomerOrders(Long id) {
        return ResponseEntity.ok(orderService.getOrdersByCustomerId(id));
    }
}

