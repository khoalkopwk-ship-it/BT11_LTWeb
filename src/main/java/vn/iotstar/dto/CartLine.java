package vn.iotstar.dto;

import vn.iotstar.entity.Video;
import java.math.BigDecimal;

public class CartLine {
    private final String videoId;
    private final String title;
    private final BigDecimal price;
    private final int quantity;
    private final int maxQuantity;
    private final String problem;

    public CartLine(String id, int quantity, Video video) {
        videoId = id;
        this.quantity = quantity;
        title = video == null ? "Sản phẩm " + id : video.getTitle();
        price = video == null || video.getPrice() == null ? BigDecimal.ZERO : video.getPrice();
        maxQuantity = video == null ? 0 : video.getMaxOrderQuantity();
        if (video == null || !video.isPurchasable()) {
            problem = "Sản phẩm đã hết hàng hoặc ngừng bán. Vui lòng xóa khỏi giỏ.";
        } else if (quantity < 1 || quantity > maxQuantity) {
            problem = "Số lượng tối đa hiện tại là " + maxQuantity + ". Vui lòng cập nhật.";
        } else {
            problem = null;
        }
    }

    public String getVideoId() { return videoId; }
    public String getTitle() { return title; }
    public BigDecimal getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public int getMaxQuantity() { return maxQuantity; }
    public String getProblem() { return problem; }
    public boolean isValid() { return problem == null; }
    public BigDecimal getLineTotal() { return price.multiply(BigDecimal.valueOf(quantity)); }
}
