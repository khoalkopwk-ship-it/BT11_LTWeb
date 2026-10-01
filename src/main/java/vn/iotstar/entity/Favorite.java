package vn.iotstar.entity;


import jakarta.persistence.*;
import java.util.Date;



@Entity
@Table(name="Favorites")
public class Favorite {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name="FavoriteId")
    private Integer favoriteId;



    @Temporal(TemporalType.DATE)

    @Column(name="LikedDate")
    private Date likedDate;



    @ManyToOne
    @JoinColumn(name="VideoId")
    private Video video;



    @ManyToOne
    @JoinColumn(name="Username")
    private User user;



}