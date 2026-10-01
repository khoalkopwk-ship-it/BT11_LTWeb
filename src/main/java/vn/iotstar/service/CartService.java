package vn.iotstar.service;

import vn.iotstar.dto.Cart;
import vn.iotstar.dto.CartLine;
import vn.iotstar.dto.CartSummary;
import vn.iotstar.entity.Video;
import java.util.ArrayList;
import java.util.function.Function;

public class CartService {
    public static final int MAX_QUANTITY = 99;
    private final Function<String, Video> findVideo;

    public CartService() { this(new VideoService()::findById); }
    public CartService(Function<String, Video> findVideo) { this.findVideo = findVideo; }

    public void add(Cart cart, String id, int quantity) {
        validateQuantity(quantity);
        int newQuantity = cart.getQuantity(id) + quantity;
        validateProduct(findVideo.apply(id), newQuantity);
        cart.put(id, newQuantity);
    }

    public void update(Cart cart, String id, int quantity) {
        if (cart.getQuantity(id) == 0) throw new IllegalArgumentException("Sản phẩm không có trong giỏ.");
        validateQuantity(quantity);
        validateProduct(findVideo.apply(id), quantity);
        cart.put(id, quantity);
    }

    public CartSummary summarize(Cart cart) {
        var lines = new ArrayList<CartLine>();
        cart.getQuantities().forEach((id, quantity) -> lines.add(new CartLine(id, quantity, findVideo.apply(id))));
        return new CartSummary(lines);
    }

    public static void validateProduct(Video video, int quantity) {
        validateQuantity(quantity);
        if (video == null || !video.isPurchasable()) {
            throw new IllegalArgumentException("Sản phẩm đã hết hàng hoặc ngừng bán.");
        }
        if (quantity > video.getStock()) {
            throw new IllegalArgumentException("Sản phẩm " + video.getTitle() + " chỉ còn " + video.getStock() + " trong kho.");
        }
    }

    private static void validateQuantity(int quantity) {
        if (quantity < 1 || quantity > MAX_QUANTITY) {
            throw new IllegalArgumentException("Số lượng mỗi sản phẩm phải từ 1 đến " + MAX_QUANTITY + ".");
        }
    }
}
