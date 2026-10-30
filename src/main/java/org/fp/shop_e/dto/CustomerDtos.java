package org.fp.shop_e.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

public final class CustomerDtos {
    private CustomerDtos() {}

    public record CustomerCreateRequest(
            @NotBlank(message = "Họ và tên không được để trống")
            String fullName,
            @NotBlank(message = "Email không được để trống")
            @Email(message = "Định dạng Email không hợp lệ")
            String email,
            @NotBlank(message = "Số điện thoại không được để trống")
            @Pattern(regexp = "^(\\+84|0)[0-9]{9,10}$", message = "Số điện thoại không hợp lệ (bắt đầu bằng 0 hoặc +84, gồm 10-11 chữ số)")
            String phone,
            String password,
            String address
    ) {}

    public record CustomerUpdateRequest(
            @NotBlank(message = "Họ và tên không được để trống")
            String fullName,
            @NotBlank(message = "Email không được để trống")
            @Email(message = "Định dạng Email không hợp lệ")
            String email,
            @NotBlank(message = "Số điện thoại không được để trống")
            @Pattern(regexp = "^(\\+84|0)[0-9]{9,10}$", message = "Số điện thoại không hợp lệ (bắt đầu bằng 0 hoặc +84, gồm 10-11 chữ số)")
            String phone,
            String address
    ) {}

    public record CustomerLoginRequest(
            @NotBlank(message = "Email không được để trống")
            @Email(message = "Định dạng Email không hợp lệ")
            String email,
            @NotBlank(message = "Mật khẩu không được để trống")
            String password
    ) {}

    public record CustomerResponse(
            Long id,
            String fullName,
            String email,
            String phone,
            String address,
            LocalDateTime createdAt
    ) {}
}

