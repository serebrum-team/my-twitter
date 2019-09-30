package uz.serebrum.mytwitter.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.serebrum.mytwitter.entity.Follower;
import uz.serebrum.mytwitter.entity.User;

import java.util.List;

@Repository
public interface UserDao extends CrudRepository<User,Long> {


    @Query(nativeQuery = true,value = "select user_member from follower where user_id = ?1")
    List<Long> getAllUserMembers(Long userId);

    @Query(nativeQuery = true,value = "select user_id from follower where user_member = ?1")
    List<Long> getAllUserFollowers(Long userId);

    @Query(nativeQuery = true,value = "select u.user_id from twitter_user as u where u.user_id in (select v.viewed_user from viewed as v where v.viewed_post_id = ?1)")
    List<Long> getAllUsersByViewedPostId(Long postId);

    User findByUserName(String username);

    boolean existsByUserNameAndPassword(String username, String password);

    void deleteByUserId(Long userId);

    boolean existsByUserName(String username);

}
