# Project brief

- Tên: `SHOP_E` (Shop E-Commerce Fullstack)
- Group: `org.fp`
- Package: `org.fp.shop_e`
- Stack: Java 21, Spring Boot 4.1.2, Spring Data JPA, Validation, OpenAPI 3.0 (`openapi-generator-maven-plugin`), Datafaker, Thymeleaf + Tailwind CSS, MySQL / H2
- Mục tiêu dự án: Xây dựng hệ thống thương mại điện tử Fullstack theo quy trình API-First gồm 3 module chính:
  1. **Module Khách hàng (`Customers`)**: Đăng ký, đăng nhập, quản lý hồ sơ cá nhân, xem lịch sử đơn hàng (validate Email & SĐT).
  2. **Module Danh mục & Sản phẩm (`Categories & Products`)**: Quản lý danh mục, sản phẩm (giá, ảnh, mô tả, `stockQuantity`), tìm kiếm, lọc theo danh mục/khoảng giá, phân trang.
  3. **Module Giỏ hàng & Đặt hàng (`Cart & Orders`)**: Giỏ hàng động, Đặt hàng (Checkout) với `@Transactional` (kiểm tra tồn kho -> tính tổng tiền -> tạo đơn `PENDING` -> trừ tồn kho) và quản lý vòng đời trạng thái đơn (`PENDING` -> `PAID` -> `SHIPPED` hoặc `CANCELLED` hoàn tồn kho).
- Tài liệu BA & Đặc tả: Đặt tại thư mục `docx/` (`docx/01_Tai_Lieu_BA.md`, `docx/02_Dac_Ta_API_He_Thong.md`, `docx/shop-api.yaml`).
