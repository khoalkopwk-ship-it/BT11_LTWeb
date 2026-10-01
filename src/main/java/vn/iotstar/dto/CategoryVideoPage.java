package vn.iotstar.dto;

import vn.iotstar.entity.Category;
import vn.iotstar.entity.Video;

import java.util.List;

public class CategoryVideoPage {
    private final Category category;
    private final List<Video> videos;
    private final long videoCount;
    private final int currentPage;
    private final int totalPages;

    public CategoryVideoPage(Category category, List<Video> videos, long videoCount,
                             int currentPage, int totalPages) {
        this.category = category;
        this.videos = videos;
        this.videoCount = videoCount;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    public Category getCategory() {
        return category;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public long getVideoCount() {
        return videoCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
