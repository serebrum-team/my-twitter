package uz.serebrum.mytwitter.request.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CommentRequestModel {

    @NotEmpty(message = "Comment is empty ")
    private String commentBody;
    @NotNull(message = "postId is null")
    private Long commentedPostId;
    @JsonIgnore
    @NotNull(message = "authorId is null")
    private Long commentAuthorId;
}
