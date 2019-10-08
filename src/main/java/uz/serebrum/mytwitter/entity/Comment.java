package uz.serebrum.mytwitter.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "COMMENT")
@JsonIgnoreProperties({"commentedPost","commentAuthorId"})
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(columnDefinition = "TEXT")
    private String commentBody;


    @CreationTimestamp
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post commentedPost;


    @ManyToOne()
    @JoinColumn(name = "COMMENT_AUTHOR_ID")
//    @Column(name = "COMMENT_AUTHOR_ID")
    private User commentAuthor;

    public Comment(String commentBody, Post commentedPost, User commentAuthor) {
        this.commentBody = commentBody;
        this.commentedPost = commentedPost;
        this.commentAuthor = commentAuthor;
    }

    public Comment() {
    }
}
