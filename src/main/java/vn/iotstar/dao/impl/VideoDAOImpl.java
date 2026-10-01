package vn.iotstar.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.dao.VideoDAO;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Video;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoDAOImpl implements VideoDAO {
    @Override
    public List<Video> findAllActive() {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            List<Video> videos = em.createQuery(
                            "select v from Video v join fetch v.category c "
                                    + "where v.active = true and c.status = true order by c.categoryname, v.title",
                            Video.class)
                    .getResultList();
            loadStatistics(em, videos);
            return videos;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> findActivePage(int page, int size) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            List<Video> videos = em.createQuery(
                            "select v from Video v join fetch v.category c "
                                    + "where v.active = true and c.status = true "
                                    + "order by c.categoryname, v.title, v.videoId",
                            Video.class)
                    .setFirstResult((normalizePage(page) - 1) * size)
                    .setMaxResults(size)
                    .getResultList();
            loadStatistics(em, videos);
            return videos;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> findPage(int page, int size) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            List<Video> videos = em.createQuery(
                            "select v from Video v left join fetch v.category order by v.videoId", Video.class)
                    .setFirstResult((normalizePage(page) - 1) * size)
                    .setMaxResults(size)
                    .getResultList();
            loadStatistics(em, videos);
            return videos;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> findActiveByCategory(int categoryId, int page, int size) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            List<Video> videos = em.createQuery(
                            "select v from Video v join fetch v.category c "
                                    + "where c.categoryId = :categoryId and v.active = true "
                                    + "order by v.title, v.videoId",
                            Video.class)
                    .setParameter("categoryId", categoryId)
                    .setFirstResult((normalizePage(page) - 1) * size)
                    .setMaxResults(size)
                    .getResultList();
            loadStatistics(em, videos);
            return videos;
        } finally {
            em.close();
        }
    }

    @Override
    public long count() {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.createQuery("select count(v) from Video v", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public long countActive() {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.createQuery(
                            "select count(v) from Video v "
                                    + "where v.active = true and v.category.status = true", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public long countActiveByCategory(int categoryId) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.createQuery(
                            "select count(v) from Video v where v.category.categoryId = :categoryId and v.active = true",
                            Long.class)
                    .setParameter("categoryId", categoryId)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public Video findById(String id) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            List<Video> result = em.createQuery(
                            "select v from Video v left join fetch v.category where v.videoId = :id", Video.class)
                    .setParameter("id", id)
                    .setMaxResults(1)
                    .getResultList();
            if (result.isEmpty()) {
                return null;
            }
            loadStatistics(em, result);
            return result.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Video video) {
        executeWrite(em -> {
            attachCategory(em, video);
            em.persist(video);
        });
    }

    @Override
    public void update(Video video) {
        executeWrite(em -> {
            attachCategory(em, video);
            em.merge(video);
        });
    }

    @Override
    public void delete(String id) {
        executeWrite(em -> {
            long ordered = em.createQuery("select count(i) from OrderItem i where i.video.videoId = :id", Long.class)
                    .setParameter("id", id).getSingleResult();
            if (ordered > 0) {
                throw new IllegalArgumentException("Sản phẩm đã có trong đơn hàng. Hãy sửa và bỏ chọn Đang hoạt động để ngừng bán.");
            }
            em.createQuery("delete from Favorite f where f.video.videoId = :id")
                    .setParameter("id", id).executeUpdate();
            em.createQuery("delete from Share s where s.video.videoId = :id")
                    .setParameter("id", id).executeUpdate();
            Video video = em.find(Video.class, id);
            if (video != null) {
                em.remove(video);
            }
        });
    }

    private void attachCategory(EntityManager em, Video video) {
        if (video.getCategory() != null && video.getCategory().getCategoryId() != null) {
            video.setCategory(em.getReference(Category.class, video.getCategory().getCategoryId()));
        }
    }

    private void loadStatistics(EntityManager em, List<Video> videos) {
        if (videos.isEmpty()) {
            return;
        }
        List<String> ids = videos.stream().map(Video::getVideoId).toList();
        Map<String, Long> favoriteCounts = groupedCounts(em,
                "select f.video.videoId, count(f) from Favorite f "
                        + "where f.video.videoId in :ids group by f.video.videoId", ids);
        Map<String, Long> shareCounts = groupedCounts(em,
                "select s.video.videoId, count(s) from Share s "
                        + "where s.video.videoId in :ids group by s.video.videoId", ids);
        for (Video video : videos) {
            video.setLikeCount(favoriteCounts.getOrDefault(video.getVideoId(), 0L));
            video.setShareCount(shareCounts.getOrDefault(video.getVideoId(), 0L));
        }
    }

    private Map<String, Long> groupedCounts(EntityManager em, String jpql, List<String> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, Long> counts = new HashMap<>();
        for (Object[] row : em.createQuery(jpql, Object[].class).setParameter("ids", ids).getResultList()) {
            counts.put((String) row[0], (Long) row[1]);
        }
        return counts;
    }

    private void executeWrite(EntityWork work) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            work.execute(em);
            transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        } finally {
            em.close();
        }
    }

    private int normalizePage(int page) {
        return Math.max(page, 1);
    }

    @FunctionalInterface
    private interface EntityWork {
        void execute(EntityManager entityManager);
    }
}
