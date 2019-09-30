package uz.serebrum.mytwitter.request.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class ViewedRequestModel {

    @NotNull(message = "postId is null")
    private Long postId;

    @JsonIgnore
    @NotNull(message = "userId is null")
    private Long viewedUserId;
}
