package vn.iotstar.controller.auth;

import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.utils.EmailUtil;
import vn.iotstar.utils.OtpUtil;

import java.io.IOException;

@WebServlet("/register")
public class RegisterController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = trim(request.getParameter("username"));
        String password = request.getParameter("password");
        String fullname = trim(request.getParameter("fullname"));
        String email = trim(request.getParameter("email"));
        String phone = trim(request.getParameter("phone"));

        if (username.length() < 3 || username.length() > 50
                || password == null || password.length() < 6 || password.length() > 50
                || fullname.isBlank() || fullname.length() > 50
                || email.length() > 150 || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
                || phone.length() > 15) {
            request.setAttribute("error", "Vui lòng nhập đúng thông tin; mật khẩu phải có ít nhất 6 ký tự.");
            request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
            return;
        }
        if (userService.findByUsername(username) != null || userService.findByEmail(email) != null) {
            request.setAttribute("error", "Tên đăng nhập hoặc email đã tồn tại.");
            request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFullname(fullname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAdmin(false);
        user.setActive(false);

        String otp = OtpUtil.generateOTP();
        try {
            EmailUtil.sendOTP(email, otp);
        } catch (MessagingException e) {
            getServletContext().log("Không thể gửi OTP", e);
            request.setAttribute("error", "Không thể gửi OTP. Hãy kiểm tra cấu hình email và thử lại.");
            request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("otp", otp);
        session.setAttribute("otpExpiresAt", System.currentTimeMillis() + 5 * 60_000L);
        session.setAttribute("userPending", user);
        response.sendRedirect(request.getContextPath() + "/verify");
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
