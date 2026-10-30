package org.fp.shop_e.config;

import java.util.List;
import java.util.Random;

/**
 * Thư viện Faker nội bộ tạo dữ liệu mẫu Tiếng Việt cho hệ thống SHOP_E
 */
public class ShopFaker {

    private final Random random = new Random(20261030L);

    private static final List<String> LAST_NAMES = List.of(
            "Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Huỳnh", "Phan", "Vũ", "Võ", "Đặng", "Bùi", "Đỗ"
    );
    private static final List<String> MIDDLE_NAMES = List.of(
            "Văn", "Thị", "Minh", "Hoàng", "Đức", "Ngọc", "Thanh", "Quốc", "Gia", "Bảo"
    );
    private static final List<String> FIRST_NAMES = List.of(
            "An", "Bình", "Châu", "Dũng", "Giang", "Hải", "Khánh", "Linh", "Minh", "Nam", "Phúc", "Quân", "Thảo", "Trang", "Vy"
    );
    private static final List<String> STREETS = List.of(
            "Nguyễn Huệ", "Lê Lợi", "Điện Biên Phủ", "Cách Mạng Tháng 8", "Võ Văn Tần",
            "Hai Bà Trưng", "Pasteur", "Nam Kỳ Khởi Nghĩa", "Phan Xích Long", "Nguyễn Văn Cừ"
    );
    private static final List<String> DISTRICTS = List.of(
            "Quận 1, TP.HCM", "Quận 3, TP.HCM", "Quận Bình Thạnh, TP.HCM",
            "TP. Thủ Đức, TP.HCM", "Quận Phú Nhuận, TP.HCM", "Quận Cầu Giấy, Hà Nội", "Quận Hải Châu, Đà Nẵng"
    );
    private static final List<String> PHONE_PREFIXES = List.of("090", "091", "093", "097", "098", "038", "079");

    public String fullName() {
        return LAST_NAMES.get(random.nextInt(LAST_NAMES.size())) + " "
                + MIDDLE_NAMES.get(random.nextInt(MIDDLE_NAMES.size())) + " "
                + FIRST_NAMES.get(random.nextInt(FIRST_NAMES.size()));
    }

    public String emailFromName(String fullName, int index) {
        String normalized = fullName.toLowerCase()
                .replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a")
                .replaceAll("[èéẹẻẽêềếệểễ]", "e")
                .replaceAll("[ìíịỉĩ]", "i")
                .replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o")
                .replaceAll("[ùúụủũưừứựửữ]", "u")
                .replaceAll("[ỳýỵỷỹ]", "y")
                .replaceAll("[đ]", "d")
                .replaceAll("\\s+", ".");
        return normalized + index + "@shope.vn";
    }

    public String phoneNumber() {
        String prefix = PHONE_PREFIXES.get(random.nextInt(PHONE_PREFIXES.size()));
        int suffix = 1000000 + random.nextInt(9000000);
        return prefix + suffix;
    }

    public String fullAddress() {
        int houseNo = 10 + random.nextInt(490);
        String street = STREETS.get(random.nextInt(STREETS.size()));
        String district = DISTRICTS.get(random.nextInt(DISTRICTS.size()));
        return houseNo + " " + street + ", " + district;
    }

    public int numberBetween(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }
}

