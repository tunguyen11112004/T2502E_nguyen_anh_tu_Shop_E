# Repo inventory

| Path | Vai trò |
| --- | --- |
| `pom.xml` | Spring Boot 4.1.2, Java 21, Spring Data JPA, Validation, H2/MySQL, OpenAPI Generator |
| `docx/01_Tai_Lieu_BA.md` | Tài liệu Phân tích Nghiệp vụ BA, User Stories, Use Cases & Sơ đồ luồng Đặt hàng/Hủy đơn |
| `docx/02_Dac_Ta_API_He_Thong.md` | Tài liệu Đặc tả Kỹ thuật, Kiến trúc SA, Thiết kế ERD & RESTful API-First |
| `docx/shop-api.yaml`, `src/main/resources/openapi/shop-api.yaml` | Đặc tả OpenAPI 3.0.3 (Single Source of Truth) |
| `docx/shop-api.postman_collection.json` | Bộ kịch bản kiểm thử hợp đồng API (Contract Test) trên Postman/Newman |
| `src/main/java/org/fp/shop_e/entity/` | Các JPA Entity: `Category`, `Product`, `Customer`, `Order`, `OrderItem`, `OrderStatus` |
| `src/main/java/org/fp/shop_e/dto/` | Các Java `record` bất biến cho Request/Response và `ErrorResponse` thống nhất |
| `src/main/java/org/fp/shop_e/openapi/api/` | Các Server Interfaces theo chuẩn OpenAPI 3.0 (`CategoriesApi`, `ProductsApi`, `CustomersApi`, `OrdersApi`) |
| `src/main/java/org/fp/shop_e/service/` | Tầng nghiệp vụ sử dụng `@Transactional(readOnly = true)` và `@Transactional` ACID |
| `src/main/java/org/fp/shop_e/controller/` | RESTful Controllers (`/api/v1/...`) và `ViewController` (`/`, `/admin`) |
| `src/main/java/org/fp/shop_e/config/` | `DataSeeder` (`CommandLineRunner`) + `ShopFaker` tự động nạp dữ liệu mẫu |
| `src/main/resources/templates/` | Giao diện `index.html` (Storefront UI) và `admin.html` (Admin Dashboard) với Tailwind CSS |
| `src/test/java/org/fp/shop_e/ShopEApplicationTests.java` | Bộ kiểm thử Unit, Integration & E2E Flow với `@SpringBootTest` & `MockMvc` |
