package vn.iotstar.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartSummary {
    private final List<CartLine> lines;
    public CartSummary(List<CartLine> lines) { this.lines = List.copyOf(lines); }
    public List<CartLine> getLines() { return lines; }
    public BigDecimal getTotalAmount() {
        return lines.stream().map(CartLine::getLineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public int getTotalQuantity() { return lines.stream().mapToInt(CartLine::getQuantity).sum(); }
    public boolean isCheckoutAllowed() { return !lines.isEmpty() && lines.stream().allMatch(CartLine::isValid); }
}
