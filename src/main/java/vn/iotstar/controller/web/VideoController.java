package vn.iotstar.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.entity.Video;
import vn.iotstar.service.VideoService;

import java.io.IOException;

@WebServlet("/videos")
public class VideoController extends HttpServlet {
    private static final int PAGE_SIZE = 3;
    private final VideoService videoService = new VideoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String id = trim(request.getParameter("id"));
        if (!id.isEmpty()) {
            Video video = videoService.findById(id);
            if (video == null || !Boolean.TRUE.equals(video.getActive()) || video.getCategory() == null
                    || !Boolean.TRUE.equals(video.getCategory().getStatus())) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy video.");
                return;
            }
            request.setAttribute("video", video);
            request.getRequestDispatcher("/views/web/detail.jsp").forward(request, response);
            return;
        }
        long totalItems = videoService.countActive();
        int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / PAGE_SIZE));
        int page = Math.min(readPositiveInt(request.getParameter("page"), 1), totalPages);
        request.setAttribute("videos", videoService.findActivePage(page, PAGE_SIZE));
        request.setAttribute("page", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalItems", totalItems);
        request.getRequestDispatcher("/views/web/videos.jsp").forward(request, response);
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private int readPositiveInt(String value, int defaultValue) {
        try {
            return Math.max(1, Integer.parseInt(value));
        } catch (NumberFormatException exception) {
            return defaultValue;
        }
    }
}
