package uz.serebrum.mytwitter.dao.solr.repository;

import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;
import uz.serebrum.mytwitter.entity.solr.entity.Post;

import java.util.List;

@Repository
public interface PostRepository extends SolrCrudRepository<Post,Long> {

    @Query("postBody : *?0* OR postTitle : *?0*")
    List<Post> searchPostsByWord(String word);
}
