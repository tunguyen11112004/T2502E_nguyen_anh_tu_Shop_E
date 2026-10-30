package org.fp.shop_e;

import org.fp.shop_e.entity.Product;
import org.fp.shop_e.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ShopEApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("1. Kiểm thử Faker Seeder & Tìm kiếm, Lọc, Phân trang Sản phẩm (GET /api/v1/products)")
    void shouldListProductsWithPaginationAndPriceFilter() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                        .param("minPrice", "10000000")
                        .param("maxPrice", "50000000")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].stockQuantity").isNumber())
                .andExpect(jsonPath("$.totalElements").isNumber());
    }

    @Test
    @DisplayName("2. Kiểm thử Đăng ký Khách hàng: Validate Email và Số điện thoại (POST /api/v1/customers)")
    void shouldValidateCustomerEmailAndPhone() throws Exception {
        // Trường hợp Email và Số điện thoại không hợp lệ -> 400 Bad Request
        String invalidJson = """
                {
                  "fullName": "Khách Hàng Lỗi",
                  "email": "email-khong-hop-le",
                  "phone": "12345"
                }
                """;

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("ERR_VALIDATION_FAILED"))
                .andExpect(jsonPath("$.details").isArray());

        // Trường hợp hợp lệ -> 201 Created
        String validJson = """
                {
                  "fullName": "Lê Hoàng Nam",
                  "email": "nam.le.test@shope.vn",
                  "phone": "0918889999",
                  "password": "123456",
                  "address": "100 Nguyễn Huệ, Quận 1, TP.HCM"
                }
                """;

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").value("nam.le.test@shope.vn"));
    }

    @Test
    @DisplayName("3. Kiểm thử Đặt hàng thành công (@Transactional trừ tồn kho) và Hủy đơn hoàn tồn kho")
    void shouldCreateOrderDeductStockAndRestoreStockWhenCancelled() throws Exception {
        Product productBefore = productRepository.findById(1L).orElseThrow();
        int initialStock = productBefore.getStockQuantity();
        int orderQty = 2;

        String orderRequestJson = """
                {
                  "customerId": 1,
                  "shippingAddress": "123 Lê Lợi, Quận 1, TP.HCM",
                  "note": "Đặt hàng test @Transactional",
                  "items": [
                    { "productId": 1, "quantity": %d }
                  ]
                }
                """.formatted(orderQty);

        // Bước 1: Đặt hàng -> 201 Created, status = PENDING
        MvcResult result = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderRequestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.orderCode").isNotEmpty())
                .andReturn();

        // Kiểm tra tồn kho đã giảm đúng orderQty
        Product productAfterOrder = productRepository.findById(1L).orElseThrow();
        assertThat(productAfterOrder.getStockQuantity()).isEqualTo(initialStock - orderQty);

        // Lấy ID đơn hàng vừa tạo
        String responseBody = result.getResponse().getContentAsString();
        Long orderId = Long.valueOf(responseBody.replaceAll(".*\"id\":(\\d+).*", "$1"));

        // Bước 2: Hủy đơn hàng (PATCH /api/v1/orders/{id} -> CANCELLED)
        mockMvc.perform(patch("/api/v1/orders/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"CANCELLED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));

        // Kiểm tra tồn kho được hoàn lại đúng bằng initialStock
        Product productAfterCancel = productRepository.findById(1L).orElseThrow();
        assertThat(productAfterCancel.getStockQuantity()).isEqualTo(initialStock);
    }

    @Test
    @DisplayName("4. Kiểm thử Đặt hàng thất bại do hết tồn kho (422 ERR_STOCK_EMPTY & Rollback không trừ kho)")
    void shouldFailCheckoutWhenStockInsufficientAndRollback() throws Exception {
        Product productBefore = productRepository.findById(2L).orElseThrow();
        int stockBefore = productBefore.getStockQuantity();

        String outOfStockOrderJson = """
                {
                  "customerId": 1,
                  "shippingAddress": "123 Lê Lợi, Quận 1",
                  "items": [
                    { "productId": 2, "quantity": 99999 }
                  ]
                }
                """;

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(outOfStockOrderJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("ERR_STOCK_EMPTY"))
                .andExpect(jsonPath("$.message").value("Sản phẩm đã hết hàng"))
                .andExpect(jsonPath("$.details").isArray());

        // Đảm bảo tồn kho không bị thay đổi
        Product productAfter = productRepository.findById(2L).orElseThrow();
        assertThat(productAfter.getStockQuantity()).isEqualTo(stockBefore);
    }

    @Test
    @DisplayName("5. Kiểm thử Vòng đời Trạng thái Đơn hàng (PENDING -> PAID -> SHIPPED & Chặn hủy sau khi SHIPPED)")
    void shouldEnforceOrderStatusStateMachine() throws Exception {
        String orderJson = """
                {
                  "customerId": 1,
                  "shippingAddress": "456 Hai Bà Trưng, Quận 3",
                  "items": [
                    { "productId": 3, "quantity": 1 }
                  ]
                }
                """;

        MvcResult created = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isCreated())
                .andReturn();

        Long orderId = Long.valueOf(created.getResponse().getContentAsString().replaceAll(".*\"id\":(\\d+).*", "$1"));

        // PENDING -> PAID (Hợp lệ)
        mockMvc.perform(patch("/api/v1/orders/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"PAID\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));

        // PAID -> SHIPPED (Hợp lệ)
        mockMvc.perform(patch("/api/v1/orders/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"SHIPPED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SHIPPED"));

        // SHIPPED -> CANCELLED (Vi phạm quy tắc -> 422 ERR_INVALID_STATUS_TRANSITION)
        mockMvc.perform(patch("/api/v1/orders/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"CANCELLED\"}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("ERR_INVALID_STATUS_TRANSITION"));
    }

    @Test
    @DisplayName("6. Kiểm thử Giao diện Storefront (/), Admin (/admin) và OpenAPI Spec (/api/v1/openapi.yaml)")
    void shouldRenderViewsAndOpenApiSpec() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/openapi.yaml"))
                .andExpect(status().isOk());
    }
}
