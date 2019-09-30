package uz.serebrum.mytwitter.request.model;

import lombok.Data;
import uz.serebrum.mytwitter.entity.embeddable.OtherUserDetails;

import javax.validation.constraints.Size;

@Data
public class UserRequestModelWithSecretCode {

    private String email;

}
