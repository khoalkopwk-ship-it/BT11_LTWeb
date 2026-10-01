package vn.iotstar.entity;

public enum OrderStatus {
    NEW("Đơn hàng mới", "primary"),
    CONFIRMED("Đã xác nhận", "info"),
    PREPARING("Chuẩn bị hàng", "warning"),
    SHIPPING("Vận chuyển", "secondary"),
    DELIVERING("Giao hàng", "info"),
    DELIVERED("Đã giao", "success"),
    CANCELLED("Đơn hàng hủy", "danger"),
    RETURNED("Đơn hàng hoàn", "dark");

    private final String label;
    private final String badge;

    OrderStatus(String label, String badge) {
        this.label = label;
        this.badge = badge;
    }

    public String getLabel() { return label; }
    public String getBadge() { return badge; }
    public String getName() { return name(); }
}
