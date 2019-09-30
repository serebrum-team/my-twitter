package uz.serebrum.mytwitter.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import uz.serebrum.mytwitter.entity.embeddable.OtherPostDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@JsonIgnoreProperties({"postComments","postVieweds","postAuthor"})
@Table(name = "POST")
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private String postTitle;
    @Embedded
    private OtherPostDetails otherPostDetails;

    @ManyToOne
    @JoinColumn(name = "POST_AUTHOR")
    private User postAuthor;

    @CreationTimestamp
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "commentedPost",fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    private List<Comment> postComments;

    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<Viewed> postVieweds;

    public Post(String postTitle, User postAuthor,OtherPostDetails otherPostDetails) {
        this.postTitle = postTitle;
        this.postAuthor = postAuthor;
        this.otherPostDetails = otherPostDetails;
    }

    public Post() {
    }
}
