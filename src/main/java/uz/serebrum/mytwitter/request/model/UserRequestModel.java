package uz.serebrum.mytwitter.request.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import uz.serebrum.mytwitter.entity.embeddable.OtherUserDetails;

import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class UserRequestModel {
    private String userName;
    private String password;

    private String firstName;
    private String lastName;

    private String bio;


    @Email
    private String email;
    private Long secretCode;
}
