package vn.iotstar.entity;


import jakarta.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name="Videos")
public class Video {


    @Id
    @Column(name="VideoId", length = 50)
    private String videoId;



    @Column(name="Title", length = 200)
    private String title;



    @Column(name="Poster", length = 50)
    private String poster;



    @Column(name="Views")
    private Integer views;



    @Column(name="Description", length = 500)
    private String description;



    @Column(name="Active")
    private Boolean active;

    @Column(name = "Price", nullable = false, precision = 18, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "Stock", nullable = false)
    private Integer stock = 0;

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public int getMaxOrderQuantity() { return Math.min(99, stock == null ? 0 : stock); }
    public boolean isPurchasable() {
        return Boolean.TRUE.equals(active) && category != null
                && Boolean.TRUE.equals(category.getStatus())
                && price != null && price.signum() > 0 && stock != null && stock > 0;
    }

    @Transient
    private long shareCount;

    @Transient
    private long likeCount;

    public String getVideoId() {
        return videoId;
    }


    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }



    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }



    public String getPoster() {
        return poster;
    }


    public void setPoster(String poster) {
        this.poster = poster;
    }



    public Integer getViews() {
        return views;
    }


    public void setViews(Integer views) {
        this.views = views;
    }



    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }



    public Boolean getActive() {
        return active;
    }


    public void setActive(Boolean active) {
        this.active = active;
    }

    public long getShareCount() {
        return shareCount;
    }

    public void setShareCount(long shareCount) {
        this.shareCount = shareCount;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }



    public Category getCategory() {
        return category;
    }


    public void setCategory(Category category) {
        this.category = category;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CategoryId")
    private Category category;

}
