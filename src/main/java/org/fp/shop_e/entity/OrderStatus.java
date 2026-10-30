package org.fp.shop_e.entity;

public enum OrderStatus {
    PENDING,
    PAID,
    SHIPPED,
    CANCELLED;

    /**
     * Kiểm tra quy tắc chuyển đổi trạng thái đơn hàng:
     * PENDING -> PAID hoặc CANCELLED
     * PAID -> SHIPPED hoặc CANCELLED
     * SHIPPED -> Không được chuyển tiếp hoặc hủy
     * CANCELLED -> Không được chuyển tiếp
     */
    public boolean canTransitionTo(OrderStatus target) {
        if (target == null || this == target) {
            return false;
        }
        return switch (this) {
            case PENDING -> target == PAID || target == CANCELLED;
            case PAID -> target == SHIPPED || target == CANCELLED;
            case SHIPPED, CANCELLED -> false;
        };
    }
}

