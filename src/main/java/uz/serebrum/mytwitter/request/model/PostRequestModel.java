package uz.serebrum.mytwitter.request.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import uz.serebrum.mytwitter.entity.embeddable.OtherPostDetails;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PostRequestModel {

    @Size(min = 1,message = "Post title must be least 1 character.")
    private String postTitle;
    private OtherPostDetails otherPostDetails;

    @JsonIgnore
    @NotNull(message = "userId is null")
    private Long postAuthorId;
}
