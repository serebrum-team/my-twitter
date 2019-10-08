package uz.serebrum.mytwitter.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "FOLLOWER")
public class Follower {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followerId;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;


//
//    @Column(name = "USER_MEMBER")
//    private Long followedUserId;

    @ManyToOne
    @JoinColumn(name = "USER_MEMBER")
    private User followedUser;



    @CreationTimestamp
    private LocalDateTime createdDate;

//    public Follower(User user, Long followedUserId) {
//        this.user = user;
//        this.followedUserId = followedUserId;
//    }


    public Follower(User user, User followedUser) {
        this.user = user;
        this.followedUser = followedUser;
    }

    public Follower() {
    }
}
