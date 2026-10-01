package vn.iotstar.dao;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;
import vn.iotstar.dao.impl.OrderDAOImpl;
import vn.iotstar.dto.CheckoutForm;
import vn.iotstar.entity.*;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderDAOTest {
    private static EntityManagerFactory factory;
    private OrderDAOImpl dao;
    private CheckoutForm form;
    private final Map<String, BigDecimal> prices = Map.of("A", new BigDecimal("10000.25"), "B", new BigDecimal("20000.50"));

    @BeforeAll
    static void open() {
        factory = Persistence.createEntityManagerFactory("DE03_LTW", Map.of(
                "jakarta.persistence.jdbc.driver", "org.h2.Driver",
                "jakarta.persistence.jdbc.url", "jdbc:h2:mem:orderdao;MODE=MSSQLServer;DB_CLOSE_DELAY=-1;LOCK_TIMEOUT=10000",
                "jakarta.persistence.jdbc.user", "sa", "jakarta.persistence.jdbc.password", "",
                "hibernate.dialect", "org.hibernate.dialect.H2Dialect", "hibernate.hbm2ddl.auto", "create-drop",
                "hibernate.show_sql", "false"));
    }
    @AfterAll static void close() { factory.close(); }

    @BeforeEach
    void seed() {
        dao = new OrderDAOImpl(factory::createEntityManager);
        write(em -> {
            for (String entity : new String[]{"OrderItem", "CustomerOrder", "Favorite", "Share", "Video", "Category", "User"}) {
                em.createQuery("delete from " + entity).executeUpdate();
            }
            Category category = new Category(); category.setCategoryname("Sản phẩm"); category.setStatus(true); em.persist(category);
            for (String name : new String[]{"user01", "user02"}) {
                User user = new User(); user.setUsername(name); user.setActive(true); user.setAdmin(false); em.persist(user);
            }
            for (String id : new String[]{"A", "B"}) {
                Video video = new Video(); video.setVideoId(id); video.setTitle("Sản phẩm " + id); video.setCategory(category);
                video.setPrice(prices.get(id)); video.setStock(20); video.setActive(true); em.persist(video);
            }
        });
        form = new CheckoutForm(); form.setReceiverName("Người dùng mẫu"); form.setPhone("0901234567");
        form.setAddress("01 Võ Văn Ngân, Thủ Đức");
    }

    @Test
    void codPersistsOrderItemsAndDeductsStockWithExactTotals() {
        Long id = place("user01", Map.of("A", 1, "B", 2), UUID.randomUUID().toString());
        CustomerOrder order = dao.findOwned(id, "user01");
        assertEquals(OrderStatus.NEW, order.getStatus());
        assertEquals("COD", order.getPaymentMethod());
        assertEquals(2, order.getItems().size());
        assertEquals(new BigDecimal("50001.25"), order.getTotalAmount());
        assertEquals(19, stock("A")); assertEquals(18, stock("B"));
    }

    @Test
    void failedSecondItemRollsBackFirstItemStockAndCreatesNoOrder() {
        assertThrows(IllegalArgumentException.class, () -> place("user01", Map.of("A", 2, "Z", 1), UUID.randomUUID().toString()));
        assertEquals(20, stock("A"));
        assertEquals(0, dao.countByUser("user01", null));
    }

    @Test
    void reusedCheckoutTokenDoesNotCreateAnotherOrderOrDeductAgain() {
        String token = UUID.randomUUID().toString();
        Long id = place("user01", Map.of("A", 2), token);
        assertEquals(id, place("user01", Map.of("A", 2), token));
        assertEquals(1, dao.countByUser("user01", null)); assertEquals(18, stock("A"));
    }

    @Test
    void priceChangeAfterReviewRejectsCheckoutWithoutStockChange() {
        write(em -> em.find(Video.class, "A").setPrice(new BigDecimal("12000.00")));
        assertThrows(IllegalArgumentException.class, () -> place("user01", Map.of("A", 1), UUID.randomUUID().toString()));
        assertEquals(20, stock("A")); assertEquals(0, dao.countByUser("user01", null));
    }

    @Test
    void orderSnapshotsSurviveProductRenameAndRepricing() {
        Long id = place("user01", Map.of("A", 2), UUID.randomUUID().toString());
        write(em -> { Video v = em.find(Video.class, "A"); v.setTitle("Tên mới"); v.setPrice(new BigDecimal("99999")); });
        CustomerOrder order = dao.findOwned(id, "user01");
        assertEquals("Sản phẩm A", order.getItems().get(0).getProductTitle());
        assertEquals(new BigDecimal("10000.25"), order.getItems().get(0).getUnitPrice());
        assertEquals(new BigDecimal("20000.50"), order.getTotalAmount());
    }

    @Test
    void allEightStatusesFilterFreshlyAndOrdersRemainPrivate() {
        for (OrderStatus status : OrderStatus.values()) {
            Long id = place("user01", Map.of("A", 1), UUID.randomUUID().toString());
            write(em -> em.createNativeQuery("UPDATE Orders SET Status = :status WHERE OrderId = :id")
                    .setParameter("status", status.name()).setParameter("id", id).executeUpdate());
        }
        Long other = place("user02", Map.of("B", 1), UUID.randomUUID().toString());
        assertNull(dao.findOwned(other, "user01"));
        assertEquals(8, dao.countByUser("user01", null));
        assertEquals(5, dao.findByUser("user01", null, 1, 5).size());
        assertEquals(3, dao.findByUser("user01", null, 2, 5).size());
        for (OrderStatus status : OrderStatus.values()) {
            assertEquals(1, dao.countByUser("user01", status));
            assertEquals(status, dao.findByUser("user01", status, 1, 5).get(0).getStatus());
            assertEquals(1L, dao.countStatuses("user01").get(status.name()));
        }
    }

    @Test
    void inactiveUserAndEmptyCartAreRejected() {
        write(em -> em.find(User.class, "user01").setActive(false));
        assertThrows(IllegalArgumentException.class, () -> place("user01", Map.of("A", 1), UUID.randomUUID().toString()));
        assertThrows(IllegalArgumentException.class, () -> place("user02", Map.of(), UUID.randomUUID().toString()));
        assertEquals(20, stock("A"));
    }

    @Test
    void twoBuyersCannotBothPurchaseLastUnit() throws Exception {
        write(em -> em.find(Video.class, "A").setStock(1));
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            Callable<Boolean> buyer = () -> {
                start.await();
                try { place("user01", Map.of("A", 1), UUID.randomUUID().toString()); return true; }
                catch (IllegalArgumentException exception) { return false; }
            };
            Future<Boolean> one = pool.submit(buyer); Future<Boolean> two = pool.submit(buyer); start.countDown();
            int successes = (one.get(20, TimeUnit.SECONDS) ? 1 : 0) + (two.get(20, TimeUnit.SECONDS) ? 1 : 0);
            assertEquals(1, successes); assertEquals(0, stock("A")); assertEquals(1, dao.countByUser("user01", null));
        } finally { pool.shutdownNow(); }
    }

    private Long place(String username, Map<String, Integer> quantities, String token) {
        return dao.placeCod(username, quantities, form, token, prices);
    }
    private int stock(String id) {
        EntityManager em = factory.createEntityManager();
        try { return em.find(Video.class, id).getStock(); } finally { em.close(); }
    }
    private void write(java.util.function.Consumer<EntityManager> work) {
        EntityManager em = factory.createEntityManager();
        try { em.getTransaction().begin(); work.accept(em); em.getTransaction().commit(); }
        finally { if (em.getTransaction().isActive()) em.getTransaction().rollback(); em.close(); }
    }
}
