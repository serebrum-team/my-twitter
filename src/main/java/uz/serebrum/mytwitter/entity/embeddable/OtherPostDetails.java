package uz.serebrum.mytwitter.entity.embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtherPostDetails {

    @Column(columnDefinition = "TEXT")
    private String postBody;


}
