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

import java.io.IOException;

@WebServlet("/verify")
public class VerifyOtpController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userPending") == null) {
            response.sendRedirect(request.getContextPath() + "/register");
            return;
        }
        request.getRequestDispatcher("/views/auth/verify.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String input = request.getParameter("otp");
        String expected = session == null ? null : (String) session.getAttribute("otp");
        Long expiresAt = session == null ? null : (Long) session.getAttribute("otpExpiresAt");
        User user = session == null ? null : (User) session.getAttribute("userPending");

        if (user == null || expected == null || expiresAt == null || System.currentTimeMillis() > expiresAt) {
            clearPending(session);
            request.setAttribute("error", "Phiên OTP đã hết hạn. Vui lòng đăng ký lại.");
            request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
            return;
        }
        if (!expected.equals(input == null ? "" : input.trim())) {
            request.setAttribute("error", "Mã OTP không chính xác.");
            request.getRequestDispatcher("/views/auth/verify.jsp").forward(request, response);
            return;
        }

        user.setActive(true);
        if (!userService.register(user)) {
            clearPending(session);
            request.setAttribute("error", "Tên đăng nhập hoặc email đã tồn tại.");
            request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
            return;
        }
        clearPending(session);
        session.setAttribute("flashSuccess", "Kích hoạt thành công. Bạn có thể đăng nhập.");
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private void clearPending(HttpSession session) {
        if (session != null) {
            session.removeAttribute("otp");
            session.removeAttribute("otpExpiresAt");
            session.removeAttribute("userPending");
        }
    }
}
