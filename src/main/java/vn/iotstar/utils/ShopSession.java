package vn.iotstar.utils;

import jakarta.servlet.http.HttpSession;
import vn.iotstar.dto.Cart;

public final class ShopSession {
    private ShopSession() { }
    public static Cart cart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
    public static void invalidateCheckout(HttpSession session) {
        session.removeAttribute("checkoutToken");
        session.removeAttribute("checkoutPrices");
    }
}
