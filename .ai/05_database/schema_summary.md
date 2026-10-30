# Schema summary

- `categories`: `id`, `name` (UK), `slug` (UK), `description`, `icon`, `created_at`
- `products`: `id`, `sku` (UK), `name`, `description`, `price`, `image_url`, `stock_quantity`, `category_id` (FK), `active`, `created_at`, `updated_at`
- `customers`: `id`, `full_name`, `email` (UK), `phone`, `password_hash`, `address`, `created_at`
- `orders`: `id`, `order_code` (UK), `customer_id` (FK), `status` (`PENDING`, `PAID`, `SHIPPED`, `CANCELLED`), `total_amount`, `shipping_address`, `note`, `created_at`, `updated_at`
- `order_items`: `id`, `order_id` (FK), `product_id` (FK), `product_name`, `unit_price`, `quantity`, `subtotal`
