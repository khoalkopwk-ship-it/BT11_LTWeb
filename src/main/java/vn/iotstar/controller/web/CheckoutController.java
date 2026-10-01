package vn.iotstar.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.dto.*;
import vn.iotstar.entity.User;
import vn.iotstar.service.CartService;
import vn.iotstar.service.OrderService;
import vn.iotstar.utils.ShopSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@WebServlet("/checkout")
public class CheckoutController extends HttpServlet {
    private final CartService carts = new CartService();
    private final OrderService orders = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        synchronized (session) {
            CartSummary summary = carts.summarize(ShopSession.cart(session));
            if (!summary.isCheckoutAllowed()) {
                session.setAttribute("shopError", "Hãy thêm sản phẩm hoặc sửa các mục không hợp lệ trước khi đặt hàng.");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }
            // Mỗi trang COD gắn với giá vừa hiển thị; biểu mẫu cũ ở tab khác sẽ hết hiệu lực.
            session.setAttribute("checkoutToken", UUID.randomUUID().toString());
            Map<String, BigDecimal> prices = new HashMap<>();
            summary.getLines().forEach(line -> prices.put(line.getVideoId(), line.getPrice()));
            session.setAttribute("checkoutPrices", prices);
            CheckoutForm form = (CheckoutForm) session.getAttribute("checkoutForm");
            if (form == null) {
                User user = (User) session.getAttribute("account");
                form = new CheckoutForm();
                form.setReceiverName(user.getFullname());
                form.setPhone(user.getPhone());
            }
            request.setAttribute("form", form);
            request.setAttribute("summary", summary);
        }
        request.getRequestDispatcher("/WEB-INF/views/shop/checkout.jsp").forward(request, response);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        synchronized (session) {
            String token = request.getParameter("checkoutToken");
            if (token != null && token.equals(session.getAttribute("lastCheckoutToken"))) {
                response.sendRedirect(request.getContextPath() + "/orders/detail?id=" + session.getAttribute("lastOrderId"));
                return;
            }
            if (token == null || !token.equals(session.getAttribute("checkoutToken"))) {
                session.setAttribute("shopError", "Giỏ hàng đã thay đổi hoặc biểu mẫu hết hạn. Vui lòng kiểm tra lại.");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }
            CheckoutForm form = new CheckoutForm();
            form.setReceiverName(request.getParameter("receiverName"));
            form.setPhone(request.getParameter("phone"));
            form.setAddress(request.getParameter("address"));
            form.setNote(request.getParameter("note"));
            session.setAttribute("checkoutForm", form);
            try {
                if (!"COD".equals(request.getParameter("paymentMethod"))) {
                    throw new IllegalArgumentException("Chỉ hỗ trợ thanh toán khi nhận hàng (COD).");
                }
                User user = (User) session.getAttribute("account");
                Cart cart = ShopSession.cart(session);
                Map<String, BigDecimal> prices = (Map<String, BigDecimal>) session.getAttribute("checkoutPrices");
                if (prices == null) throw new IllegalArgumentException("Vui lòng tải lại trang thanh toán.");
                Long id = orders.placeCod(user.getUsername(), cart.getQuantities(), form, token, prices);
                cart.clear();
                ShopSession.invalidateCheckout(session);
                session.removeAttribute("checkoutForm");
                session.setAttribute("lastCheckoutToken", token);
                session.setAttribute("lastOrderId", id);
                session.setAttribute("shopSuccess", "Đặt hàng COD thành công. Bạn sẽ thanh toán khi nhận hàng.");
                response.sendRedirect(request.getContextPath() + "/orders/detail?id=" + id);
                return;
            } catch (IllegalArgumentException exception) {
                session.setAttribute("shopError", exception.getMessage());
            } catch (RuntimeException exception) {
                getServletContext().log("Không thể tạo đơn hàng COD", exception);
                session.setAttribute("shopError", "Không thể đặt hàng. Đơn chưa được tạo; vui lòng thử lại.");
            }
        }
        response.sendRedirect(request.getContextPath() + "/checkout");
    }
}
