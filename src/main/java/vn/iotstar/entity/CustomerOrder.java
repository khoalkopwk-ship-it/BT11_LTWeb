package vn.iotstar.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Orders")
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderId")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Username", nullable = false)
    private User user;

    @Column(name = "ReceiverName", nullable = false, length = 100)
    private String receiverName;
    @Column(name = "Phone", nullable = false, length = 15)
    private String phone;
    @Column(name = "Address", nullable = false, length = 500)
    private String address;
    @Column(name = "Note", length = 500)
    private String note;
    @Column(name = "PaymentMethod", nullable = false, length = 10)
    private String paymentMethod = "COD";
    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false, length = 20)
    private OrderStatus status = OrderStatus.NEW;
    @Column(name = "CreatedAt", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "TotalAmount", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;
    @Column(name = "CheckoutToken", nullable = false, unique = true, length = 36)
    private String checkoutToken;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    @OrderBy("orderItemId ASC")
    private List<OrderItem> items = new ArrayList<>();

    public void addItem(OrderItem item) {
        item.setOrder(this);
        items.add(item);
        totalAmount = totalAmount.add(item.getLineTotal());
    }

    public Long getOrderId() { return orderId; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getPaymentMethod() { return paymentMethod; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getCreatedAtText() {
        return createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getCheckoutToken() { return checkoutToken; }
    public void setCheckoutToken(String checkoutToken) { this.checkoutToken = checkoutToken; }
    public List<OrderItem> getItems() { return items; }
}
