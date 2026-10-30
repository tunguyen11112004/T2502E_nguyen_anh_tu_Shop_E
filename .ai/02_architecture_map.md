# Architecture map

```
Browser (Storefront UI `/` & Admin Dashboard `/admin` - Tailwind CSS + Fetch API)
        |
        v
RESTful Controllers (`/api/v1/categories`, `/api/v1/products`, `/api/v1/customers`, `/api/v1/orders`)
  + GlobalExceptionHandler (Unified JSON Error: {"code", "message", "details"})
        |
        v
Service Layer (`@Transactional(readOnly = true)` cho truy vấn đọc, `@Transactional` ACID cho Checkout & Hủy đơn)
  - CategoryService
  - ProductService
  - CustomerService
  - OrderService
        |
        v
Spring Data JPA Repositories + DataSeeder (CommandLineRunner + Faker)
        |
        v
Database (H2 MySQL-mode mặc định / MySQL 8.x qua profile `mysql`)
```
