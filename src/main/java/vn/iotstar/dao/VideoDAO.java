package vn.iotstar.dao;

import vn.iotstar.entity.Video;

import java.util.List;

public interface VideoDAO {
    List<Video> findAllActive();

    List<Video> findActivePage(int page, int size);

    List<Video> findPage(int page, int size);

    List<Video> findActiveByCategory(int categoryId, int page, int size);

    long count();

    long countActive();

    long countActiveByCategory(int categoryId);

    Video findById(String id);

    void save(Video video);

    void update(Video video);

    void delete(String id);
}
