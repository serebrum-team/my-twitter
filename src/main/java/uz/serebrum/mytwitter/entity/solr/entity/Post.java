package uz.serebrum.mytwitter.entity.solr.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;


@Data
@SolrDocument(collection = "post")
@NoArgsConstructor
public class Post {

    @Id
    @Indexed(name = "id")
    private Long id;

    @Indexed(name = "postTitle")
    private String postTitle;


    @Indexed(name = "postBody")
    private String postBody;

    public Post(Long id, String postTitle, String postBody) {
        this.id = id;
        this.postTitle = postTitle;
        this.postBody = postBody;
    }
}
