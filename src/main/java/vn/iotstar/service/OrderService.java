package vn.iotstar.service;

import vn.iotstar.dao.OrderDAO;
import vn.iotstar.dao.impl.OrderDAOImpl;
import vn.iotstar.dto.CheckoutForm;
import vn.iotstar.entity.CustomerOrder;
import vn.iotstar.entity.OrderStatus;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class OrderService {
    private final OrderDAO dao;
    public OrderService() { this(new OrderDAOImpl()); }
    public OrderService(OrderDAO dao) { this.dao = dao; }

    public Long placeCod(String username, Map<String, Integer> quantities, CheckoutForm form,
                         String token, Map<String, BigDecimal> prices) {
        validateForm(form);
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Vui lòng đăng nhập.");
        if (token == null || !token.matches("[a-f0-9-]{36}")) throw new IllegalArgumentException("Phiên đặt hàng không hợp lệ.");
        return dao.placeCod(username, quantities, form, token, prices);
    }

    public void validateForm(CheckoutForm form) {
        if (form == null || form.getReceiverName().length() < 2 || form.getReceiverName().length() > 100) {
            throw new IllegalArgumentException("Tên người nhận phải từ 2 đến 100 ký tự.");
        }
        if (!form.getPhone().matches("0[0-9]{9}")) {
            throw new IllegalArgumentException("Số điện thoại phải có 10 chữ số và bắt đầu bằng 0.");
        }
        if (form.getAddress().length() < 10 || form.getAddress().length() > 500) {
            throw new IllegalArgumentException("Địa chỉ nhận hàng phải từ 10 đến 500 ký tự.");
        }
        if (form.getNote().length() > 500) throw new IllegalArgumentException("Ghi chú tối đa 500 ký tự.");
    }

    public List<CustomerOrder> findByUser(String username, OrderStatus status, int page, int size) {
        return dao.findByUser(username, status, page, size);
    }
    public long countByUser(String username, OrderStatus status) { return dao.countByUser(username, status); }
    public Map<String, Long> countStatuses(String username) { return dao.countStatuses(username); }
    public CustomerOrder findOwned(Long id, String username) { return dao.findOwned(id, username); }
}
