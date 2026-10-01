package vn.iotstar.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.dto.Cart;
import vn.iotstar.service.CartService;
import vn.iotstar.utils.ShopSession;
import java.io.IOException;

@WebServlet({"/cart", "/cart/add", "/cart/update", "/cart/remove", "/cart/clear"})
public class CartController extends HttpServlet {
    private final CartService service = new CartService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!"/cart".equals(request.getServletPath())) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        HttpSession session = request.getSession();
        synchronized (session) {
            request.setAttribute("summary", service.summarize(ShopSession.cart(session)));
        }
        request.getRequestDispatcher("/WEB-INF/views/shop/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        synchronized (session) {
            Cart cart = ShopSession.cart(session);
            try {
                String id = request.getParameter("videoId");
                switch (request.getServletPath()) {
                    case "/cart/add" -> service.add(cart, validId(id), quantity(request));
                    case "/cart/update" -> service.update(cart, validId(id), quantity(request));
                    case "/cart/remove" -> cart.remove(validId(id));
                    case "/cart/clear" -> cart.clear();
                    default -> {
                        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                        return;
                    }
                }
                ShopSession.invalidateCheckout(session);
                session.setAttribute("shopSuccess", "Đã cập nhật giỏ hàng.");
            } catch (IllegalArgumentException exception) {
                session.setAttribute("shopError", exception.getMessage());
            } catch (RuntimeException exception) {
                getServletContext().log("Không thể cập nhật giỏ hàng", exception);
                session.setAttribute("shopError", "Không thể cập nhật giỏ hàng. Vui lòng thử lại.");
            }
        }
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private int quantity(HttpServletRequest request) {
        try { return Integer.parseInt(request.getParameter("quantity")); }
        catch (NumberFormatException exception) { throw new IllegalArgumentException("Số lượng phải là số nguyên từ 1 đến 99."); }
    }
    private String validId(String id) {
        if (id == null || !id.matches("[A-Za-z0-9_-]{1,50}")) throw new IllegalArgumentException("Mã sản phẩm không hợp lệ.");
        return id;
    }
}
