package vn.iotstar.web;

import jakarta.persistence.EntityManager;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.loader.WebappLoader;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.junit.jupiter.api.*;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.entity.*;
import java.math.BigDecimal;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.*;

class WebFlowTest {
    private static Tomcat tomcat;
    private static String base;
    private HttpClient client;

    @BeforeAll
    static void start() throws Exception {
        System.setProperty("db.url", "jdbc:h2:mem:webdemo;MODE=MSSQLServer;DB_CLOSE_DELAY=-1");
        System.setProperty("db.username", "sa");
        System.setProperty("db.password", "");
        System.setProperty("db.driver", "org.h2.Driver");
        System.setProperty("db.dialect", "org.hibernate.dialect.H2Dialect");
        System.setProperty("db.schema", "create-drop");
        seed();
        tomcat = new Tomcat();
        tomcat.setBaseDir(Files.createTempDirectory("ecomart-tomcat-").toString());
        tomcat.setPort(0);
        tomcat.getConnector();
        Context context = tomcat.addWebapp("", Path.of("src/main/webapp").toAbsolutePath().toString());
        context.setParentClassLoader(WebFlowTest.class.getClassLoader());
        WebappLoader loader = new WebappLoader();
        loader.setDelegate(true);
        context.setLoader(loader);
        StandardRoot resources = new StandardRoot(context);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", Path.of("target/classes").toAbsolutePath().toString(), "/"));
        context.setResources(resources);
        tomcat.start();
        base = "http://127.0.0.1:" + tomcat.getConnector().getLocalPort();
        System.out.println("TEST_WEB_URL=" + base);
    }

    @AfterAll
    static void stop() throws Exception {
        if (tomcat != null) { tomcat.stop(); tomcat.destroy(); }
        JpaConfig.close();
        for (String key : List.of("db.url", "db.username", "db.password", "db.driver", "db.dialect", "db.schema")) {
            System.clearProperty(key);
        }
    }

    @Test
    void completeCartCodHistoryAndOwnershipFlowRendersThroughSiteMesh() throws Exception {
        client = newClient();
        HttpResponse<String> products = get("/videos");
        Files.writeString(Path.of("target/test-products.html"), products.body());
        assertEquals(200, products.statusCode());
        assertTrue(products.body().contains("Thêm vào giỏ"));
        assertTrue(products.body().contains("/assets/vendor/bootstrap/bootstrap.min.css"));
        assertTrue(products.body().contains("MSSV: 24110253"));
        assertFalse(products.body().contains("<sitemesh:write"));
        String csrf = field(products.body(), "shopToken");
        assertEquals(403, post("/cart/add", Map.of("videoId", "SP001", "quantity", "1")).statusCode());
        cartPost("add", csrf, "SP005", "2");
        assertTrue(get("/cart").body().contains("158.000"));
        cartPost("update", csrf, "SP005", "3");
        assertTrue(get("/cart").body().contains("237.000"));
        cartPost("add", csrf, "SP005", "1");
        assertTrue(get("/cart").body().contains("chỉ còn 3"));
        for (String invalid : new String[]{"0", "100", "abc"}) {
            cartPost("update", csrf, "SP005", invalid);
            assertTrue(get("/cart").body().contains("237.000"));
        }
        cartPost("add", csrf, "SP006", "1");
        assertTrue(get("/cart").body().contains("hết hàng"));
        cartPost("remove", csrf, "SP005", "1");
        assertTrue(get("/cart").body().contains("Giỏ hàng đang trống"));
        cartPost("add", csrf, "SP001", "2");
        cartPost("add", csrf, "SP005", "1");
        assertEquals("/login", location(get("/checkout")));
        assertEquals("/checkout", location(post("/login", Map.of("username", "user01", "password", "123456"))));
        HttpResponse<String> checkout = get("/checkout");
        assertEquals(200, checkout.statusCode());
        assertTrue(checkout.body().contains("257.000"));
        String token = field(checkout.body(), "checkoutToken");
        Map<String, String> form = new HashMap<>(Map.of(
                "shopToken", csrf, "checkoutToken", token, "paymentMethod", "COD",
                "receiverName", "Người nhận mẫu", "phone", "123", "address", "01 Võ Văn Ngân, Thủ Đức", "note", "Giao buổi sáng"));
        assertEquals("/checkout", location(post("/checkout", form)));
        String correctedCheckout = get("/checkout").body();
        assertTrue(correctedCheckout.contains("10 chữ số"));
        form.put("checkoutToken", field(correctedCheckout, "checkoutToken"));
        assertEquals(8, countOrders("user01"));
        form.put("phone", "0901234567");
        HttpResponse<String> placed = post("/checkout", form);
        assertEquals(302, placed.statusCode());
        String detailPath = location(placed);
        assertTrue(detailPath.startsWith("/orders/detail?id="));
        long orderId = Long.parseLong(detailPath.substring(detailPath.indexOf('=') + 1));
        assertEquals(9, countOrders("user01"));
        assertEquals(8, stock("SP001")); assertEquals(2, stock("SP005"));
        assertEquals(detailPath, location(post("/checkout", form)));
        assertEquals(9, countOrders("user01")); assertEquals(8, stock("SP001"));
        String detail = get(detailPath).body();
        assertTrue(detail.contains("Đặt hàng COD thành công"));
        assertTrue(detail.contains("257.000")); assertTrue(detail.contains("Giao buổi sáng"));
        assertTrue(get("/cart").body().contains("Giỏ hàng đang trống"));
        assertEquals(200, get("/orders?page=2").statusCode());
        assertEquals(400, get("/orders?status=BAD").statusCode());
        assertEquals(404, get("/orders/detail?id=abc").statusCode());
        for (OrderStatus status : OrderStatus.values()) {
            write(em -> em.createNativeQuery("UPDATE Orders SET Status = :status WHERE OrderId = :id")
                    .setParameter("status", status.name()).setParameter("id", orderId).executeUpdate());
            HttpResponse<String> filtered = get("/orders?status=" + status.name());
            assertEquals(200, filtered.statusCode());
            assertTrue(filtered.body().contains("/orders/detail?id=" + orderId), status.name());
            assertTrue(get(detailPath).body().contains(status.getLabel()));
        }
        write(em -> { Video video = em.find(Video.class, "SP001"); video.setTitle("Tên đã đổi"); video.setPrice(new BigDecimal("999999")); });
        assertTrue(get(detailPath).body().contains("Bình nước EcoMart"));
        assertTrue(get(detailPath).body().contains("257.000"));
        get("/logout");
        assertEquals("/login", location(get("/orders")));
        assertEquals("/orders", location(post("/login", Map.of("username", "user02", "password", "123456"))));
        assertEquals(404, get(detailPath).statusCode());
        assertFalse(get("/orders").body().contains("/orders/detail?id=" + orderId));
        assertTrue(get("/cart").body().contains("Giỏ hàng đang trống"));
        get("/logout");
        post("/login", Map.of("username", "admin", "password", "123456"));
        assertEquals(200, get("/admin/videos").statusCode());
        assertTrue(get("/admin/video/edit?id=SP001").body().contains("name=\"price\""));
        HttpResponse<String> deletion = post("/admin/video/delete", Map.of("id", "SP001"));
        assertEquals(302, deletion.statusCode());
        assertTrue(get("/admin/videos").body().contains("Sản phẩm đã có trong đơn hàng"));
    }

    private static void seed() {
        write(em -> {
            Category category = new Category(); category.setCategoryname("Sản phẩm"); category.setCategorycode("SP"); category.setStatus(true); em.persist(category);
            for (String name : new String[]{"user01", "user02", "admin"}) {
                User user = new User(); user.setUsername(name); user.setPassword("123456"); user.setFullname("Người dùng " + name);
                user.setPhone("0901234567"); user.setAdmin(name.equals("admin")); user.setActive(true); em.persist(user);
            }
            for (String id : new String[]{"SP001", "SP005", "SP006"}) {
                Video video = new Video(); video.setVideoId(id); video.setTitle(id.equals("SP001") ? "Bình nước EcoMart" : "Hộp EcoMart " + id);
                video.setPrice(new BigDecimal(id.equals("SP001") ? "89000" : "79000"));
                video.setStock(id.equals("SP001") ? 10 : id.equals("SP005") ? 3 : 0);
                video.setViews(10); video.setActive(true); video.setCategory(category); em.persist(video);
            }
            for (OrderStatus status : OrderStatus.values()) {
                CustomerOrder order = new CustomerOrder(); order.setUser(em.find(User.class, "user01")); order.setReceiverName("Người nhận demo");
                order.setPhone("0901234567"); order.setAddress("01 Võ Văn Ngân, Thủ Đức"); order.setStatus(status);
                order.setCheckoutToken(UUID.randomUUID().toString());
                Video video = em.find(Video.class, "SP001");
                OrderItem item = new OrderItem(); item.setVideo(video); item.setProductTitle(video.getTitle()); item.setUnitPrice(video.getPrice()); item.setQuantity(1);
                order.addItem(item); em.persist(order);
            }
        });
    }
    private HttpClient newClient() { return HttpClient.newBuilder().cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL)).build(); }
    private HttpResponse<String> get(String path) throws Exception {
        HttpResponse<String> response = client.send(HttpRequest.newBuilder(URI.create(base + path)).GET().build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        Files.writeString(Path.of("target/test-last-response.html"), response.body());
        return response;
    }
    private HttpResponse<String> post(String path, Map<String, String> fields) throws Exception {
        StringJoiner body = new StringJoiner("&");
        fields.forEach((key, value) -> body.add(URLEncoder.encode(key, StandardCharsets.UTF_8) + "=" + URLEncoder.encode(value, StandardCharsets.UTF_8)));
        return client.send(HttpRequest.newBuilder(URI.create(base + path)).header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString())).build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }
    private void cartPost(String action, String csrf, String id, String quantity) throws Exception {
        assertEquals(302, post("/cart/" + action, Map.of("shopToken", csrf, "videoId", id, "quantity", quantity)).statusCode());
    }
    private String field(String html, String name) {
        var matcher = Pattern.compile("name=\"" + name + "\" value=\"([^\"]+)\"").matcher(html);
        assertTrue(matcher.find(), "Missing field " + name);
        return matcher.group(1);
    }
    private String location(HttpResponse<String> response) { return response.headers().firstValue("Location").orElse("").replace(base, ""); }
    private long countOrders(String user) {
        EntityManager em = JpaConfig.getEntityManager();
        try { return em.createQuery("select count(o) from CustomerOrder o where o.user.username = :username", Long.class).setParameter("username", user).getSingleResult(); }
        finally { em.close(); }
    }
    private int stock(String id) { EntityManager em = JpaConfig.getEntityManager(); try { return em.find(Video.class, id).getStock(); } finally { em.close(); } }
    private static void write(java.util.function.Consumer<EntityManager> work) {
        EntityManager em = JpaConfig.getEntityManager();
        try { em.getTransaction().begin(); work.accept(em); em.getTransaction().commit(); }
        finally { if (em.getTransaction().isActive()) em.getTransaction().rollback(); em.close(); }
    }
}
