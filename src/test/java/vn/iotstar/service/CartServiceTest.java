package vn.iotstar.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import vn.iotstar.dto.Cart;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Video;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class CartServiceTest {
    private Cart cart;
    private CartService service;
    private Video video;
    private Map<String, Video> products;

    @BeforeEach
    void setup() {
        cart = new Cart();
        products = new HashMap<>();
        Category category = new Category();
        category.setStatus(true);
        video = new Video();
        video.setVideoId("A");
        video.setTitle("Bình nước");
        video.setPrice(new BigDecimal("10000.25"));
        video.setStock(5);
        video.setActive(true);
        video.setCategory(category);
        products.put("A", video);
        service = new CartService(products::get);
    }

    @Test
    void addMergesSameProductAndCalculatesExactMoney() {
        service.add(cart, "A", 2);
        service.add(cart, "A", 1);
        assertEquals(1, cart.getQuantities().size());
        assertEquals(3, cart.getTotalQuantity());
        assertEquals(new BigDecimal("30000.75"), service.summarize(cart).getTotalAmount());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 100, Integer.MAX_VALUE})
    void rejectInvalidQuantityWithoutChangingCart(int quantity) {
        assertThrows(IllegalArgumentException.class, () -> service.add(cart, "A", quantity));
        assertTrue(cart.isEmpty());
    }

    @Test
    void accumulatedQuantityCannotExceedStock() {
        service.add(cart, "A", 4);
        assertThrows(IllegalArgumentException.class, () -> service.add(cart, "A", 2));
        assertEquals(4, cart.getQuantity("A"));
    }

    @Test
    void limit99AppliesEvenWhenStockIsHigher() {
        video.setStock(200);
        service.add(cart, "A", 99);
        assertThrows(IllegalArgumentException.class, () -> service.add(cart, "A", 1));
        assertEquals(99, cart.getQuantity("A"));
    }

    @Test
    void editRemoveAndClearWork() {
        service.add(cart, "A", 4);
        service.update(cart, "A", 2);
        assertEquals(2, cart.getQuantity("A"));
        cart.remove("A");
        assertTrue(cart.isEmpty());
        service.add(cart, "A", 1);
        cart.clear();
        assertTrue(cart.isEmpty());
    }

    @Test
    void changedStockOrDeletedProductBlocksCheckout() {
        service.add(cart, "A", 4);
        video.setStock(2);
        assertFalse(service.summarize(cart).isCheckoutAllowed());
        service.update(cart, "A", 2);
        assertTrue(service.summarize(cart).isCheckoutAllowed());
        products.clear();
        assertFalse(service.summarize(cart).isCheckoutAllowed());
        assertDoesNotThrow(() -> cart.remove("A"));
    }

    @Test
    void inactiveProductOrCategoryCannotBeAdded() {
        video.setActive(false);
        assertThrows(IllegalArgumentException.class, () -> service.add(cart, "A", 1));
        video.setActive(true);
        video.getCategory().setStatus(false);
        assertThrows(IllegalArgumentException.class, () -> service.add(cart, "A", 1));
    }

    @Test
    void emptyCartCannotCheckoutAndSummaryUsesCurrentPrice() {
        assertFalse(service.summarize(cart).isCheckoutAllowed());
        service.add(cart, "A", 2);
        video.setPrice(new BigDecimal("9000.00"));
        assertEquals(new BigDecimal("18000.00"), service.summarize(cart).getTotalAmount());
    }
}
