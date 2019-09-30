package uz.serebrum.mytwitter.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "VIEWED")
public class Viewed {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long viewedId;

    @ManyToOne
    @JoinColumn(name = "VIEWED_POST_ID")
    private Post post;



    @CreationTimestamp
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "VIEWED_USER")
    private User viewedUser;

    public Viewed(Post post, User viewedUser) {
        this.post = post;
        this.viewedUser = viewedUser;
    }

    public Viewed() {
    }
}
