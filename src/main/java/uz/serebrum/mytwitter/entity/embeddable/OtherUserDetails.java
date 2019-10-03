package uz.serebrum.mytwitter.entity.embeddable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.Date;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtherUserDetails {

    private String firstName;
    private String lastName;

    @Column(columnDefinition = "TEXT")
    private String bio;


    @Email
    private String email;


}
