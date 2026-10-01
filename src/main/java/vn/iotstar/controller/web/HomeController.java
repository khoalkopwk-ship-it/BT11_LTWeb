package vn.iotstar.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.dto.CategoryVideoPage;
import vn.iotstar.entity.Category;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.VideoService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet({"", "/home"})
public class HomeController extends HttpServlet {
    private static final int PAGE_SIZE = 3;
    private final VideoService videoService = new VideoService();
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        List<CategoryVideoPage> groups = new ArrayList<>();
        for (Category category : categoryService.findAllActive()) {
            long count = videoService.countActiveByCategory(category.getCategoryId());
            int totalPages = Math.max(1, (int) Math.ceil((double) count / PAGE_SIZE));
            int currentPage = Math.min(readPositiveInt(
                    request.getParameter("page_" + category.getCategoryId()), 1), totalPages);
            groups.add(new CategoryVideoPage(
                    category,
                    videoService.findActiveByCategory(category.getCategoryId(), currentPage, PAGE_SIZE),
                    count,
                    currentPage,
                    totalPages));
        }
        request.setAttribute("categoryPages", groups);
        request.getRequestDispatcher("/views/web/home.jsp").forward(request, response);
    }

    private int readPositiveInt(String value, int defaultValue) {
        try {
            return Math.max(1, Integer.parseInt(value));
        } catch (NumberFormatException exception) {
            return defaultValue;
        }
    }
}
