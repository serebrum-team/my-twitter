package uz.serebrum.mytwitter.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.serebrum.mytwitter.entity.Viewed;

import java.util.List;

@Repository
public interface ViewedDao extends CrudRepository<Viewed,Long> {

    @Query(nativeQuery = true,value = "select count(*) from twitter_user as u where u.user_id in (select v.viewed_user from viewed as v where v.viewed_post_id = ?1)")
    Long countViewedUsersByPostId(Long postId);

    List<Viewed> findAllByViewedUser_UserId(Long viewedUser_userId);

    List<Viewed> findAllByPost_PostId(Long post_postId);
}
