package vn.iotstar.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.TypedQuery;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.dao.OrderDAO;
import vn.iotstar.dto.CheckoutForm;
import vn.iotstar.entity.*;
import vn.iotstar.service.CartService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class OrderDAOImpl implements OrderDAO {
    private final Supplier<EntityManager> entityManagers;
    public OrderDAOImpl() { this(JpaConfig::getEntityManager); }
    public OrderDAOImpl(Supplier<EntityManager> entityManagers) { this.entityManagers = entityManagers; }

    @Override
    public Long placeCod(String username, Map<String, Integer> quantities, CheckoutForm form,
                         String token, Map<String, BigDecimal> expectedPrices) {
        EntityManager em = entityManagers.get();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            List<CustomerOrder> previous = em.createQuery(
                    "select o from CustomerOrder o where o.checkoutToken = :token and o.user.username = :username",
                    CustomerOrder.class).setParameter("token", token).setParameter("username", username).getResultList();
            if (!previous.isEmpty()) {
                tx.commit();
                return previous.get(0).getOrderId();
            }
            User user = em.find(User.class, username);
            if (user == null || !Boolean.TRUE.equals(user.getActive())) {
                throw new IllegalArgumentException("Tài khoản không còn hoạt động. Vui lòng đăng nhập lại.");
            }
            if (quantities.isEmpty()) throw new IllegalArgumentException("Giỏ hàng đang trống.");

            CustomerOrder order = new CustomerOrder();
            order.setUser(user);
            order.setReceiverName(form.getReceiverName());
            order.setPhone(form.getPhone());
            order.setAddress(form.getAddress());
            order.setNote(form.getNote());
            order.setCheckoutToken(token);

            // Khóa theo thứ tự mã, kiểm tra và trừ kho trong cùng một transaction.
            for (String id : quantities.keySet().stream().sorted().toList()) {
                Video video = em.find(Video.class, id, LockModeType.PESSIMISTIC_WRITE);
                int quantity = quantities.get(id);
                CartService.validateProduct(video, quantity);
                BigDecimal expected = expectedPrices.get(id);
                if (expected == null || expected.compareTo(video.getPrice()) != 0) {
                    throw new IllegalArgumentException("Giá sản phẩm đã thay đổi. Vui lòng kiểm tra lại giỏ hàng.");
                }
                OrderItem item = new OrderItem();
                item.setVideo(video);
                item.setProductTitle(video.getTitle());
                item.setUnitPrice(video.getPrice());
                item.setQuantity(quantity);
                order.addItem(item);
                video.setStock(video.getStock() - quantity);
            }
            if (order.getTotalAmount().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
                throw new IllegalArgumentException("Tổng tiền vượt giới hạn cho phép.");
            }
            em.persist(order);
            tx.commit();
            return order.getOrderId();
        } catch (RuntimeException exception) {
            if (tx.isActive()) tx.rollback();
            throw exception;
        } finally {
            em.close();
        }
    }

    @Override
    public List<CustomerOrder> findByUser(String username, OrderStatus status, int page, int size) {
        EntityManager em = entityManagers.get();
        try {
            TypedQuery<CustomerOrder> query = em.createQuery(
                    "select o from CustomerOrder o where o.user.username = :username"
                            + (status == null ? "" : " and o.status = :status")
                            + " order by o.createdAt desc, o.orderId desc", CustomerOrder.class);
            query.setParameter("username", username);
            if (status != null) query.setParameter("status", status);
            return query.setFirstResult((page - 1) * size).setMaxResults(size).getResultList();
        } finally { em.close(); }
    }

    @Override
    public long countByUser(String username, OrderStatus status) {
        EntityManager em = entityManagers.get();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "select count(o) from CustomerOrder o where o.user.username = :username"
                            + (status == null ? "" : " and o.status = :status"), Long.class);
            query.setParameter("username", username);
            if (status != null) query.setParameter("status", status);
            return query.getSingleResult();
        } finally { em.close(); }
    }

    @Override
    public Map<String, Long> countStatuses(String username) {
        EntityManager em = entityManagers.get();
        try {
            Map<String, Long> counts = new HashMap<>();
            for (OrderStatus status : OrderStatus.values()) counts.put(status.name(), 0L);
            for (Object[] row : em.createQuery(
                    "select o.status, count(o) from CustomerOrder o where o.user.username = :username group by o.status",
                    Object[].class).setParameter("username", username).getResultList()) {
                counts.put(((OrderStatus) row[0]).name(), (Long) row[1]);
            }
            return counts;
        } finally { em.close(); }
    }

    @Override
    public CustomerOrder findOwned(Long id, String username) {
        EntityManager em = entityManagers.get();
        try {
            return em.createQuery(
                    "select distinct o from CustomerOrder o left join fetch o.items i left join fetch i.video"
                            + " where o.orderId = :id and o.user.username = :username", CustomerOrder.class)
                    .setParameter("id", id).setParameter("username", username)
                    .getResultStream().findFirst().orElse(null);
        } finally { em.close(); }
    }
}
