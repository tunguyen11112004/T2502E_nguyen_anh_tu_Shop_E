package org.fp.shop_e.openapi.api;

import jakarta.validation.Valid;
import org.fp.shop_e.dto.CustomerDtos.CustomerCreateRequest;
import org.fp.shop_e.dto.CustomerDtos.CustomerLoginRequest;
import org.fp.shop_e.dto.CustomerDtos.CustomerResponse;
import org.fp.shop_e.dto.CustomerDtos.CustomerUpdateRequest;
import org.fp.shop_e.dto.OrderDtos.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * OpenAPI 3.0 Server Interface cho tài nguyên /api/v1/customers
 */
@RequestMapping("/api/v1/customers")
public interface CustomersApi {

    @GetMapping
    ResponseEntity<List<CustomerResponse>> getCustomers();

    @GetMapping("/{id}")
    ResponseEntity<CustomerResponse> getCustomerById(@PathVariable("id") Long id);

    @PostMapping
    ResponseEntity<CustomerResponse> registerCustomer(@Valid @RequestBody CustomerCreateRequest request);

    @PostMapping("/sessions")
    ResponseEntity<CustomerResponse> createCustomerSession(@Valid @RequestBody CustomerLoginRequest request);

    @PutMapping("/{id}")
    ResponseEntity<CustomerResponse> updateCustomer(@PathVariable("id") Long id, @Valid @RequestBody CustomerUpdateRequest request);

    @GetMapping("/{id}/orders")
    ResponseEntity<List<OrderResponse>> getCustomerOrders(@PathVariable("id") Long id);
}

