package vn.iotstar.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.CustomerOrder;
import vn.iotstar.entity.OrderStatus;
import vn.iotstar.entity.User;
import vn.iotstar.service.OrderService;
import java.io.IOException;

@WebServlet({"/orders", "/orders/detail"})
public class OrderController extends HttpServlet {
    private static final int PAGE_SIZE = 5;
    private final OrderService service = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("account");
        if ("/orders/detail".equals(request.getServletPath())) {
            Long id;
            try { id = Long.valueOf(request.getParameter("id")); }
            catch (NumberFormatException exception) { response.sendError(404); return; }
            CustomerOrder order = service.findOwned(id, user.getUsername());
            if (order == null) { response.sendError(404, "Không tìm thấy đơn hàng của bạn."); return; }
            request.setAttribute("order", order);
            request.getRequestDispatcher("/WEB-INF/views/shop/order-detail.jsp").forward(request, response);
            return;
        }
        OrderStatus status = null;
        String statusParam = request.getParameter("status");
        if (statusParam != null && !statusParam.isBlank()) {
            try { status = OrderStatus.valueOf(statusParam); }
            catch (IllegalArgumentException exception) { response.sendError(400, "Trạng thái đơn không hợp lệ."); return; }
        }
        long total = service.countByUser(user.getUsername(), status);
        int totalPages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));
        int page;
        try { page = Math.min(Math.max(1, Integer.parseInt(request.getParameter("page"))), totalPages); }
        catch (NumberFormatException exception) { page = 1; }
        request.setAttribute("orders", service.findByUser(user.getUsername(), status, page, PAGE_SIZE));
        request.setAttribute("statuses", OrderStatus.values());
        request.setAttribute("statusCounts", service.countStatuses(user.getUsername()));
        request.setAttribute("selectedStatus", status == null ? "" : status.name());
        request.setAttribute("totalItems", total);
        request.setAttribute("allItems", service.countByUser(user.getUsername(), null));
        request.setAttribute("page", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("/WEB-INF/views/shop/orders.jsp").forward(request, response);
    }
}
