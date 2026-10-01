package vn.iotstar.entity;


import jakarta.persistence.*;
import java.util.Date;



@Entity
@Table(name="Shares")
public class Share {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name="ShareId")
    private Integer shareId;



    @Column(name="Emails", length = 50)
    private String emails;



    @Temporal(TemporalType.DATE)

    @Column(name="SharedDate")
    private Date sharedDate;



    @ManyToOne
    @JoinColumn(name="Username")
    private User user;



    @ManyToOne
    @JoinColumn(name="VideoId")
    private Video video;



}
