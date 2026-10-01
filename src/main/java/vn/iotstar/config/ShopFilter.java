package vn.iotstar.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import vn.iotstar.entity.User;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class ShopFilter implements Filter {
    private static final Set<String> WRITES = Set.of("/cart/add", "/cart/update", "/cart/remove", "/cart/clear", "/checkout");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getServletPath();
        if (path.startsWith("/assets/") || path.startsWith("/WEB-INF/")) {
            chain.doFilter(request, response);
            return;
        }
        HttpSession session = req.getSession();
        synchronized (session) {
            if (session.getAttribute("shopCsrfToken") == null) {
                session.setAttribute("shopCsrfToken", UUID.randomUUID().toString());
            }
        }
        boolean protectedPath = path.equals("/checkout") || path.equals("/orders") || path.startsWith("/orders/");
        if (protectedPath) {
            User user = (User) session.getAttribute("account");
            if (user == null || !Boolean.TRUE.equals(user.getActive())) {
                session.setAttribute("shopReturnTo", path.equals("/checkout") ? "/checkout" : "/orders");
                session.setAttribute("shopError", "Vui lòng đăng nhập để đặt hàng hoặc xem lịch sử đơn.");
                res.sendRedirect(req.getContextPath() + "/login");
                return;
            }
        }
        if ("POST".equals(req.getMethod()) && WRITES.contains(path)
                && !session.getAttribute("shopCsrfToken").equals(req.getParameter("shopToken"))) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Phiên biểu mẫu không hợp lệ. Vui lòng tải lại trang.");
            return;
        }
        if (path.startsWith("/cart") || protectedPath) {
            res.setHeader("Cache-Control", "no-store");
        }
        if ("GET".equals(req.getMethod())) {
            moveFlash(session, req, "shopSuccess");
            moveFlash(session, req, "shopError");
        }
        chain.doFilter(request, response);
    }

    private void moveFlash(HttpSession session, HttpServletRequest request, String name) {
        Object value = session.getAttribute(name);
        if (value != null) {
            request.setAttribute(name, value);
            session.removeAttribute(name);
        }
    }
}
