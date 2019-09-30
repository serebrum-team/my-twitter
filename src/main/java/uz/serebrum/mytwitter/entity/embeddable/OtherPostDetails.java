package uz.serebrum.mytwitter.entity.embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtherPostDetails {

    private String postBody;


}
