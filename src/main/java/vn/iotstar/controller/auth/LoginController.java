package vn.iotstar.controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.utils.ShopSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            moveFlash(session, request, "flashError", "error");
            moveFlash(session, request, "flashSuccess", "success");
        }
        request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = userService.login(username == null ? "" : username.trim(), password == null ? "" : password);
        if (user == null) {
            request.setAttribute("error", "Sai thông tin đăng nhập hoặc tài khoản chưa kích hoạt.");
            request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        request.changeSessionId();
        User previous = (User) session.getAttribute("account");
        if (previous != null && !previous.getUsername().equals(user.getUsername())) {
            session.removeAttribute("cart");
            session.removeAttribute("checkoutForm");
            session.removeAttribute("lastCheckoutToken");
            session.removeAttribute("lastOrderId");
            ShopSession.invalidateCheckout(session);
        }
        session.setAttribute("account", user);
        String returnTo = (String) session.getAttribute("shopReturnTo");
        session.removeAttribute("shopReturnTo");
        String next = ("/checkout".equals(returnTo) || "/orders".equals(returnTo))
                ? returnTo : (Boolean.TRUE.equals(user.getAdmin()) ? "/admin" : "/home");
        response.sendRedirect(request.getContextPath() + next);
    }

    private void moveFlash(HttpSession session, HttpServletRequest request, String from, String to) {
        Object value = session.getAttribute(from);
        if (value != null) {
            request.setAttribute(to, value);
            session.removeAttribute(from);
        }
    }
}
