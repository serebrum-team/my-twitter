package uz.serebrum.mytwitter.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.serebrum.mytwitter.entity.Follower;
import uz.serebrum.mytwitter.entity.User;

import java.util.List;

@Repository
public interface FollowerDao extends CrudRepository<Follower,Long> {

    boolean existsByUser_UserIdAndFollowedUserId(Long user_userId, Long followedUserId);

    void deleteByUser_UserIdAndFollowedUserId(Long user_userId, Long followedUserId);


}
