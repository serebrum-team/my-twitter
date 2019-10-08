package uz.serebrum.mytwitter.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.serebrum.mytwitter.entity.Comment;
import uz.serebrum.mytwitter.entity.User;

import java.util.List;

@Repository
public interface CommentDao extends CrudRepository<Comment,Long> {

    @Query(nativeQuery = true,value = "SELECT c.comment_id FROM comment AS c where c.post_id = ?1")
    List<Long> findAllByCommentedPost_PostId(Long commentedPost_postId);

    boolean existsByCommentIdAndCommentAuthor(Long commentId, User commentAuthor);

}
