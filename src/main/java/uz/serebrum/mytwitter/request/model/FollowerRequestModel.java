package uz.serebrum.mytwitter.request.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FollowerRequestModel {
    @NotNull(message = "userId is null")
    private Long userId;

    @JsonIgnore
    @NotNull(message = "memberId is null")
    private Long memberUserId;
}
