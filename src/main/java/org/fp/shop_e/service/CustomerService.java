package org.fp.shop_e.service;

import org.fp.shop_e.dto.CustomerDtos.CustomerCreateRequest;
import org.fp.shop_e.dto.CustomerDtos.CustomerLoginRequest;
import org.fp.shop_e.dto.CustomerDtos.CustomerResponse;
import org.fp.shop_e.dto.CustomerDtos.CustomerUpdateRequest;
import org.fp.shop_e.entity.Customer;
import org.fp.shop_e.exception.BusinessException;
import org.fp.shop_e.exception.ResourceNotFoundException;
import org.fp.shop_e.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ERR_CUSTOMER_NOT_FOUND", "Không tìm thấy khách hàng với ID: " + id));
        return toResponse(customer);
    }

    @Transactional
    public CustomerResponse registerCustomer(CustomerCreateRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase();
        if (customerRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new BusinessException(
                    "ERR_DUPLICATE_EMAIL",
                    "Email đã được đăng ký cho tài khoản khác",
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    List.of("Email '" + normalizedEmail + "' đã tồn tại trong hệ thống")
            );
        }
        String password = (request.password() != null && !request.password().isBlank())
                ? request.password().trim()
                : "123456";
        Customer customer = new Customer(
                request.fullName().trim(),
                normalizedEmail,
                request.phone().trim(),
                password,
                request.address() != null ? request.address().trim() : "TP. Hồ Chí Minh"
        );
        return toResponse(customerRepository.save(customer));
    }

    public CustomerResponse authenticate(CustomerLoginRequest request) {
        Customer customer = customerRepository.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(() -> new BusinessException(
                        "ERR_INVALID_CREDENTIALS",
                        "Email hoặc mật khẩu không chính xác",
                        HttpStatus.UNPROCESSABLE_ENTITY
                ));
        if (!customer.getPasswordHash().equals(request.password())) {
            throw new BusinessException(
                    "ERR_INVALID_CREDENTIALS",
                    "Email hoặc mật khẩu không chính xác",
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }
        return toResponse(customer);
    }

    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerUpdateRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ERR_CUSTOMER_NOT_FOUND", "Không tìm thấy khách hàng với ID: " + id));

        String normalizedEmail = request.email().trim().toLowerCase();
        customerRepository.findByEmailIgnoreCase(normalizedEmail)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BusinessException(
                            "ERR_DUPLICATE_EMAIL",
                            "Email đã thuộc về một khách hàng khác",
                            HttpStatus.UNPROCESSABLE_ENTITY
                    );
                });

        customer.setFullName(request.fullName().trim());
        customer.setEmail(normalizedEmail);
        customer.setPhone(request.phone().trim());
        if (request.address() != null) {
            customer.setAddress(request.address().trim());
        }
        return toResponse(customerRepository.save(customer));
    }

    public CustomerResponse toResponse(Customer c) {
        return new CustomerResponse(
                c.getId(),
                c.getFullName(),
                c.getEmail(),
                c.getPhone(),
                c.getAddress(),
                c.getCreatedAt()
        );
    }
}

