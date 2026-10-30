package org.fp.shop_e.config;

import org.fp.shop_e.dto.OrderDtos.OrderCreateRequest;
import org.fp.shop_e.dto.OrderDtos.OrderItemRequest;
import org.fp.shop_e.dto.OrderDtos.OrderStatusUpdateRequest;
import org.fp.shop_e.entity.Category;
import org.fp.shop_e.entity.Customer;
import org.fp.shop_e.entity.OrderStatus;
import org.fp.shop_e.entity.Product;
import org.fp.shop_e.repository.CategoryRepository;
import org.fp.shop_e.repository.CustomerRepository;
import org.fp.shop_e.repository.OrderRepository;
import org.fp.shop_e.repository.ProductRepository;
import org.fp.shop_e.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Database Seeding: CommandLineRunner kết hợp Faker tự động nạp dữ liệu mẫu
 * (Danh mục, Sản phẩm mẫu, Khách hàng, Đơn hàng mẫu) khi ứng dụng khởi chạy.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public DataSeeder(
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            CustomerRepository customerRepository,
            OrderRepository orderRepository,
            OrderService orderService
    ) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            return;
        }

        ShopFaker faker = new ShopFaker();
        log.info("🌱 [SHOP_E Faker Seeder] Đang khởi tạo dữ liệu mẫu E-Commerce...");

        // 1. Seed Categories
        Category catLaptop = categoryRepository.save(new Category(
                "Laptop & Macbook", "laptop-macbook",
                "Laptop văn phòng, đồ họa, gaming và hệ sinh thái Apple MacBook chính hãng", "laptop"
        ));
        Category catPhone = categoryRepository.save(new Category(
                "Điện thoại & Tablet", "dien-thoai-tablet",
                "Smartphone flagship, máy tính bảng hiệu năng cao phục vụ công việc và giải trí", "smartphone"
        ));
        Category catAudio = categoryRepository.save(new Category(
                "Âm thanh & Tai nghe", "am-thanh-tai-nghe",
                "Tai nghe chống ồn chủ động ANC, loa Bluetooth Hi-Res Audio", "headphones"
        ));
        Category catGear = categoryRepository.save(new Category(
                "Phụ kiện & Gaming Gear", "phu-kien-gaming-gear",
                "Bàn phím cơ không dây, chuột công thái học, màn hình 4K chuyên nghiệp", "keyboard"
        ));
        Category catWatch = categoryRepository.save(new Category(
                "Đồng hồ Thông minh", "dong-ho-thong-minh",
                "Smartwatch theo dõi sức khỏe, thể thao và định vị GPS đa băng tần", "watch"
        ));

        // 2. Seed Products (12 sản phẩm mẫu đa dạng tồn kho và phân khúc giá)
        List<Product> products = List.of(
                new Product(
                        "SKU-LAP-001",
                        "MacBook Pro 14 M3 Pro 18GB/512GB",
                        "Chip Apple M3 Pro 11-core CPU, màn hình Liquid Retina XDR 120Hz, thời lượng pin 18 giờ chuyên nghiệp.",
                        new BigDecimal("49990000"),
                        "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=800&auto=format&fit=crop&q=80",
                        12, catLaptop
                ),
                new Product(
                        "SKU-LAP-002",
                        "Laptop ASUS ROG Zephyrus G16 OLED",
                        "Intel Core Ultra 9, RTX 4070 8GB, màn hình ROG Nebula OLED 2.5K 240Hz siêu mỏng nhẹ.",
                        new BigDecimal("45990000"),
                        "https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=800&auto=format&fit=crop&q=80",
                        8, catLaptop
                ),
                new Product(
                        "SKU-LAP-003",
                        "Dell XPS 13 Plus 9320 Touch OLED",
                        "Thiết kế tràn viền vô cực, Intel Core i7 Gen 13, RAM 16GB LPDDR5, SSD 1TB NVMe tốc độ cao.",
                        new BigDecimal("36500000"),
                        "https://images.unsplash.com/photo-1593642632823-8f785ba67e45?w=800&auto=format&fit=crop&q=80",
                        15, catLaptop
                ),
                new Product(
                        "SKU-PHO-001",
                        "iPhone 16 Pro Max 256GB Titan Tự Nhiên",
                        "Chip A18 Pro tiến trình 3nm, Camera Fusion 48MP quay 4K 120fps Dolby Vision, nút Camera Control.",
                        new BigDecimal("34990000"),
                        "https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=800&auto=format&fit=crop&q=80",
                        20, catPhone
                ),
                new Product(
                        "SKU-PHO-002",
                        "Samsung Galaxy S24 Ultra 5G 12GB/512GB",
                        "Khung viền Titanium bền bỉ, tích hợp bộ công cụ Galaxy AI thông minh và bút S-Pen quyền năng.",
                        new BigDecimal("29990000"),
                        "https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=800&auto=format&fit=crop&q=80",
                        18, catPhone
                ),
                new Product(
                        "SKU-PHO-003",
                        "iPad Pro M4 11 inch WiFi 256GB",
                        "Màn hình Ultra Retina XDR Tandem OLED mỏng nhất lịch sử Apple, sức mạnh xử lý AI vượt trội.",
                        new BigDecimal("27990000"),
                        "https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=800&auto=format&fit=crop&q=80",
                        10, catPhone
                ),
                new Product(
                        "SKU-AUD-001",
                        "Tai nghe Sony WH-1000XM5 Wireless ANC",
                        "Đỉnh cao chống ồn chủ động với 8 micro và bộ xử lý V1, âm thanh Hi-Res LDAC, pin 30 giờ.",
                        new BigDecimal("7490000"),
                        "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&auto=format&fit=crop&q=80",
                        25, catAudio
                ),
                new Product(
                        "SKU-AUD-002",
                        "Apple AirPods Pro 2 USB-C MagSafe",
                        "Chip H2 khử tiếng ồn gấp 2 lần, âm thanh không gian cá nhân hóa, hộp sạc chuẩn IP54.",
                        new BigDecimal("5990000"),
                        "https://images.unsplash.com/photo-1600294037681-c80b4cb5b434?w=800&auto=format&fit=crop&q=80",
                        30, catAudio
                ),
                new Product(
                        "SKU-GEA-001",
                        "Bàn phím cơ Keychron Q1 Pro QMK Wireless",
                        "Khung nhôm CNC nguyên khối, cấu trúc Gasket Mount êm ái, switch K-Pro Banana hot-swap.",
                        new BigDecimal("4890000"),
                        "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=800&auto=format&fit=crop&q=80",
                        14, catGear
                ),
                new Product(
                        "SKU-GEA-002",
                        "Chuột không dây Logitech MX Master 3S",
                        "Cảm biến Darkfield 8000 DPI trên mặt kính, cuộn từ tính MagSpeed siêu êm, kết nối 3 thiết bị.",
                        new BigDecimal("2490000"),
                        "https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=800&auto=format&fit=crop&q=80",
                        40, catGear
                ),
                new Product(
                        "SKU-WAT-001",
                        "Apple Watch Ultra 2 49mm GPS + Cellular",
                        "Vỏ Titan chuẩn hàng không vũ trụ, màn hình 3000 nits, GPS tần số kép chính xác tuyệt đối.",
                        new BigDecimal("20990000"),
                        "https://images.unsplash.com/photo-1579586337278-3befd40fd17a?w=800&auto=format&fit=crop&q=80",
                        9, catWatch
                ),
                new Product(
                        "SKU-WAT-002",
                        "Garmin Fenix 8 Sapphire Solar 47mm",
                        "Đồng hồ đa môn thể thao cao cấp, sạc năng lượng mặt trời, bản đồ TopoActive tích hợp.",
                        new BigDecimal("26990000"),
                        "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800&auto=format&fit=crop&q=80",
                        3, catWatch
                )
        );
        List<Product> savedProducts = productRepository.saveAll(products);

        // 3. Seed Customers (1 tài khoản demo cố định + 5 tài khoản sinh tự động bằng Faker)
        Customer demoCustomer = customerRepository.save(new Customer(
                "Nguyễn Văn An (Khách hàng Demo)",
                "an.nguyen@shope.vn",
                "0901234567",
                "123456",
                "123 Nguyễn Huệ, Quận 1, TP.HCM"
        ));

        for (int i = 1; i <= 5; i++) {
            String fullName = faker.fullName();
            customerRepository.save(new Customer(
                    fullName,
                    faker.emailFromName(fullName, i),
                    faker.phoneNumber(),
                    "123456",
                    faker.fullAddress()
            ));
        }

        // 4. Seed Sample Orders ở các trạng thái PENDING, PAID, SHIPPED
        if (orderRepository.count() == 0 && !savedProducts.isEmpty()) {
            // Đơn 1: PENDING
            orderService.createOrder(new OrderCreateRequest(
                    demoCustomer.getId(),
                    demoCustomer.getAddress(),
                    "Giao trong giờ hành chính, gọi trước 15 phút",
                    List.of(
                            new OrderItemRequest(savedProducts.get(6).getId(), 1),
                            new OrderItemRequest(savedProducts.get(9).getId(), 1)
                    )
            ));

            // Đơn 2: PAID
            var paidOrder = orderService.createOrder(new OrderCreateRequest(
                    demoCustomer.getId(),
                    demoCustomer.getAddress(),
                    "Đã thanh toán qua chuyển khoản ngân hàng",
                    List.of(new OrderItemRequest(savedProducts.get(3).getId(), 1))
            ));
            orderService.updateOrderStatus(paidOrder.id(), new OrderStatusUpdateRequest(OrderStatus.PAID));

            // Đơn 3: SHIPPED
            var shippedOrder = orderService.createOrder(new OrderCreateRequest(
                    demoCustomer.getId(),
                    "88 Lê Lợi, Quận 1, TP.HCM",
                    "Đơn hàng giao nhanh nội thành",
                    List.of(new OrderItemRequest(savedProducts.get(7).getId(), 2))
            ));
            orderService.updateOrderStatus(shippedOrder.id(), new OrderStatusUpdateRequest(OrderStatus.PAID));
            orderService.updateOrderStatus(shippedOrder.id(), new OrderStatusUpdateRequest(OrderStatus.SHIPPED));
        }

        log.info("✅ [SHOP_E Faker Seeder] Hoàn tất: {} danh mục, {} sản phẩm, {} khách hàng, {} đơn hàng.",
                categoryRepository.count(), productRepository.count(), customerRepository.count(), orderRepository.count());
    }
}

