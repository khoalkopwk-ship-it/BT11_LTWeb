package vn.iotstar.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;

import java.io.IOException;

public class AdminFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("account");
        if (user != null && Boolean.TRUE.equals(user.getAdmin()) && Boolean.TRUE.equals(user.getActive())) {
            chain.doFilter(request, response);
            return;
        }
        if (session != null) {
            session.removeAttribute("account");
            session.setAttribute("flashError", "Bạn cần đăng nhập bằng tài khoản quản trị.");
        }
        res.sendRedirect(req.getContextPath() + "/login");
    }
}
