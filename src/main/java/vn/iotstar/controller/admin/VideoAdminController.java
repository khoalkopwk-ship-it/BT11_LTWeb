package vn.iotstar.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Video;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.VideoService;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet({
        "/admin/videos",
        "/admin/video/create",
        "/admin/video/view",
        "/admin/video/edit",
        "/admin/video/save",
        "/admin/video/delete"
})
public class VideoAdminController extends HttpServlet {
    private static final int PAGE_SIZE = 6;
    private final VideoService videoService = new VideoService();
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        switch (request.getServletPath()) {
            case "/admin/video/create" -> showForm(request, response, new Video(), false);
            case "/admin/video/edit" -> showExisting(request, response, false);
            case "/admin/video/view" -> showExisting(request, response, true);
            default -> showList(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        switch (request.getServletPath()) {
            case "/admin/video/save" -> save(request, response);
            case "/admin/video/delete" -> delete(request, response);
            default -> response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    private void showList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long totalItems = videoService.count();
        int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / PAGE_SIZE));
        int page = Math.min(readPositiveInt(request.getParameter("page"), 1), totalPages);
        moveFlash(request, "flashSuccess", "success");
        moveFlash(request, "flashError", "error");
        request.setAttribute("videos", videoService.findPage(page, PAGE_SIZE));
        request.setAttribute("page", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalItems", totalItems);
        request.getRequestDispatcher("/views/admin/video/list.jsp").forward(request, response);
    }

    private void showExisting(HttpServletRequest request, HttpServletResponse response, boolean readOnly)
            throws IOException, ServletException {
        Video video = videoService.findById(trim(request.getParameter("id")));
        if (video == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy video.");
            return;
        }
        if (readOnly) {
            request.setAttribute("video", video);
            request.getRequestDispatcher("/views/admin/video/view.jsp").forward(request, response);
        } else {
            showForm(request, response, video, true);
        }
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response, Video video, boolean editing)
            throws ServletException, IOException {
        request.setAttribute("video", video);
        request.setAttribute("editing", editing);
        request.setAttribute("categories", categoryService.findAll());
        request.getRequestDispatcher("/views/admin/video/form.jsp").forward(request, response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean editing = "edit".equals(request.getParameter("mode"));
        String videoId = trim(request.getParameter("videoId"));
        String title = trim(request.getParameter("title"));
        String poster = trim(request.getParameter("poster"));
        String description = trim(request.getParameter("description"));
        Integer views = parseNonNegativeInt(request.getParameter("views"));
        Integer categoryId = parsePositiveInt(request.getParameter("categoryId"));
        Category category = categoryId == null ? null : categoryService.findById(categoryId);

        Video video = new Video();
        video.setVideoId(videoId);
        video.setTitle(title);
        video.setPoster(poster);
        video.setDescription(description);
        video.setViews(views);
        video.setActive(request.getParameter("active") != null);
        video.setCategory(category);
        video.setPrice(parsePrice(request.getParameter("price")));
        video.setStock(parseNonNegativeInt(request.getParameter("stock")));

        String error = validate(video);
        Video existing = videoId.isEmpty() ? null : videoService.findById(videoId);
        if (error == null && editing && existing == null) {
            error = "Video cần cập nhật không còn tồn tại.";
        } else if (error == null && !editing && existing != null) {
            error = "Mã video đã tồn tại.";
        }
        if (error != null) {
            request.setAttribute("error", error);
            showForm(request, response, video, editing);
            return;
        }

        try {
            if (editing) {
                videoService.update(video);
                setFlash(request, "flashSuccess", "Cập nhật video thành công.");
            } else {
                videoService.save(video);
                setFlash(request, "flashSuccess", "Thêm video thành công.");
            }
            response.sendRedirect(request.getContextPath() + "/admin/videos");
        } catch (RuntimeException exception) {
            getServletContext().log("Không thể lưu video " + videoId, exception);
            request.setAttribute("error", "Không thể lưu video. Vui lòng kiểm tra dữ liệu và thử lại.");
            showForm(request, response, video, editing);
        }
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = trim(request.getParameter("id"));
        try {
            if (videoService.findById(id) == null) {
                setFlash(request, "flashError", "Video không tồn tại.");
            } else {
                videoService.delete(id);
                setFlash(request, "flashSuccess", "Đã xóa video " + id + ".");
            }
        } catch (IllegalArgumentException exception) {
            setFlash(request, "flashError", exception.getMessage());
        } catch (RuntimeException exception) {
            getServletContext().log("Không thể xóa video " + id, exception);
            setFlash(request, "flashError", "Không thể xóa video.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/videos");
    }

    private String validate(Video video) {
        if (!video.getVideoId().matches("[A-Za-z0-9_-]{1,50}")) {
            return "Mã video gồm 1–50 ký tự chữ, số, gạch ngang hoặc gạch dưới.";
        }
        if (video.getTitle().isEmpty() || video.getTitle().length() > 200) {
            return "Tiêu đề là bắt buộc và không quá 200 ký tự.";
        }
        if (video.getPoster().length() > 50) {
            return "Đường dẫn poster không quá 50 ký tự theo cấu trúc database.";
        }
        if (video.getDescription().length() > 500) {
            return "Mô tả không quá 500 ký tự.";
        }
        if (video.getViews() == null) {
            return "Lượt xem phải là số nguyên không âm.";
        }
        if (video.getCategory() == null) {
            return "Vui lòng chọn Category hợp lệ.";
        }
        if (video.getPrice() == null) return "Giá phải lớn hơn 0, có tối đa 2 chữ số thập phân và nằm trong giới hạn DECIMAL(18,2).";
        if (video.getStock() == null) return "Tồn kho phải là số nguyên không âm.";
        return null;
    }

    private void setFlash(HttpServletRequest request, String key, String value) {
        request.getSession().setAttribute(key, value);
    }

    private void moveFlash(HttpServletRequest request, String from, String to) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(from) != null) {
            request.setAttribute(to, session.getAttribute(from));
            session.removeAttribute(from);
        }
    }

    private int readPositiveInt(String value, int defaultValue) {
        Integer result = parsePositiveInt(value);
        return result == null ? defaultValue : result;
    }

    private Integer parsePositiveInt(String value) {
        try {
            int result = Integer.parseInt(value);
            return result > 0 ? result : null;
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private Integer parseNonNegativeInt(String value) {
        try {
            int result = Integer.parseInt(value);
            return result >= 0 ? result : null;
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private BigDecimal parsePrice(String value) {
        try {
            if (value == null || !value.matches("[0-9]{1,16}(\\.[0-9]{1,2})?")) return null;
            BigDecimal price = new BigDecimal(value);
            return price.signum() > 0 ? price : null;
        } catch (NumberFormatException exception) { return null; }
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
