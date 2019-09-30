package uz.serebrum.mytwitter.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import uz.serebrum.mytwitter.entity.Post;

import java.util.List;

public interface PostDao extends CrudRepository<Post, Long> {


    @Query(nativeQuery = true, value = "SELECT p.post_id FROM post AS p where p.post_author = ?1")
    List<Long> getAllByPostAuthor_UserId(Long postAuthor_userId);


    @Query(nativeQuery = true, value = "select post_id from post where post_author in " +
            "(select user_id from follower where user_member = ?1) and " +
            "post_id not in (select viewed_post_id from viewed where viewed_user = ?1);")
    List<Long> getAllUnViewedPostsListByUserId(Long userId);


    @Query(nativeQuery = true, value = "select post_id \n" +
            "from post\n" +
            "where post_id in (select v.viewed_post_id\n" +
            "                  from viewed as v\n" +
            "                  where v.viewed_post_id not in (select viewed_post_id from viewed where viewed_user = ?1)\n" +
            "                  group by v.viewed_post_id\n" +
            "                  order by count(*) desc\n" +
            "                  limit 3)")
    List<Long> getHotTop3UnViewedPosts(Long userId);


    boolean existsByPostAuthor_UserIdAndPostId(Long postAuthor_userId, Long postId);

}
