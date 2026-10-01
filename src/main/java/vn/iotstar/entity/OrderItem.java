package vn.iotstar.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "OrderItems")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderItemId")
    private Long orderItemId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OrderId", nullable = false)
    private CustomerOrder order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VideoId", nullable = false)
    private Video video;

    // Lưu tên và giá lúc đặt để lịch sử không đổi khi quản trị sửa sản phẩm.
    @Column(name = "ProductTitle", nullable = false, length = 200)
    private String productTitle;
    @Column(name = "UnitPrice", nullable = false, precision = 18, scale = 2)
    private BigDecimal unitPrice;
    @Column(name = "Quantity", nullable = false)
    private int quantity;

    public Long getOrderItemId() { return orderItemId; }
    public CustomerOrder getOrder() { return order; }
    public void setOrder(CustomerOrder order) { this.order = order; }
    public Video getVideo() { return video; }
    public void setVideo(Video video) { this.video = video; }
    public String getProductTitle() { return productTitle; }
    public void setProductTitle(String productTitle) { this.productTitle = productTitle; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getLineTotal() { return unitPrice.multiply(BigDecimal.valueOf(quantity)); }
}
