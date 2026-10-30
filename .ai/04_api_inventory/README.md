# API inventory (`/api/v1`)

| Method | Endpoint | Mô tả | Status Code |
| --- | --- | --- | --- |
| `GET` | `/api/v1/categories` | Danh sách danh mục | `200 OK` |
| `POST` | `/api/v1/categories` | Tạo danh mục mới | `201 Created` |
| `PUT` | `/api/v1/categories/{id}` | Cập nhật danh mục | `200 OK` |
| `DELETE` | `/api/v1/categories/{id}` | Xóa danh mục | `204 No Content` |
| `GET` | `/api/v1/products` | Tìm kiếm, lọc theo danh mục/khoảng giá, phân trang | `200 OK` |
| `GET` | `/api/v1/products/{id}` | Chi tiết sản phẩm | `200 OK`, `404` |
| `POST` | `/api/v1/products` | Thêm sản phẩm mới | `201 Created` |
| `PUT` | `/api/v1/products/{id}` | Cập nhật sản phẩm | `200 OK` |
| `PATCH` | `/api/v1/products/{id}` | Cập nhật nhanh tồn kho (`stockQuantity`) / giá bán | `200 OK` |
| `DELETE` | `/api/v1/products/{id}` | Xóa mềm sản phẩm | `204 No Content` |
| `GET` | `/api/v1/customers` | Danh sách khách hàng | `200 OK` |
| `POST` | `/api/v1/customers` | Đăng ký khách hàng (validate Email & SĐT) | `201 Created`, `400`, `422` |
| `POST` | `/api/v1/customers/sessions` | Đăng nhập khách hàng | `200 OK`, `422` |
| `PUT` | `/api/v1/customers/{id}` | Cập nhật thông tin cá nhân | `200 OK`, `400`, `404` |
| `GET` | `/api/v1/customers/{id}/orders` | Xem lịch sử đơn hàng của khách hàng | `200 OK`, `404` |
| `GET` | `/api/v1/orders` | Danh sách đơn hàng (lọc `status`, `customerId`, phân trang) | `200 OK` |
| `GET` | `/api/v1/orders/{id}` | Chi tiết đơn hàng | `200 OK`, `404` |
| `POST` | `/api/v1/orders` | Đặt hàng (Checkout ACID `@Transactional`, trừ tồn kho) | `201 Created`, `422 ERR_STOCK_EMPTY` |
| `PATCH` | `/api/v1/orders/{id}` | Đổi trạng thái đơn (`PENDING -> PAID -> SHIPPED` / `CANCELLED` hoàn kho) | `200 OK`, `422` |
| `GET` | `/api/v1/openapi.yaml` | Phục vụ file đặc tả OpenAPI 3.0.3 | `200 OK` |
