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
    @Column(name = "COMMENT_AUTHOR_ID")
    private Long commentAuthorId;

    public Comment(String commentBody, Post commentedPost, Long commentAuthorId) {
        this.commentBody = commentBody;
        this.commentedPost = commentedPost;
        this.commentAuthorId = commentAuthorId;
    }

    public Comment() {
    }
}
