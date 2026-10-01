package vn.iotstar.service;

import vn.iotstar.dao.VideoDAO;
import vn.iotstar.dao.impl.VideoDAOImpl;
import vn.iotstar.entity.Video;

import java.util.List;

public class VideoService {
    private final VideoDAO dao = new VideoDAOImpl();

    public List<Video> findAllActive() {
        return dao.findAllActive();
    }

    public List<Video> findActivePage(int page, int size) {
        return dao.findActivePage(page, size);
    }

    public List<Video> findPage(int page, int size) {
        return dao.findPage(page, size);
    }

    public List<Video> findActiveByCategory(int categoryId, int page, int size) {
        return dao.findActiveByCategory(categoryId, page, size);
    }

    public long count() {
        return dao.count();
    }

    public long countActive() {
        return dao.countActive();
    }

    public long countActiveByCategory(int categoryId) {
        return dao.countActiveByCategory(categoryId);
    }

    public Video findById(String id) {
        return dao.findById(id);
    }

    public void save(Video video) {
        dao.save(video);
    }

    public void update(Video video) {
        dao.update(video);
    }

    public void delete(String id) {
        dao.delete(id);
    }
}
