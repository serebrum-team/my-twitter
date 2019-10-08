package uz.serebrum.mytwitter.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.serebrum.mytwitter.entity.Follower;
import uz.serebrum.mytwitter.entity.User;

import java.util.List;

@Repository
public interface FollowerDao extends CrudRepository<Follower,Long> {

    boolean existsByUser_UserIdAndFollowedUser(Long user_userId, User followedUser);

    void deleteByUser_UserIdAndFollowedUser(Long user_userId, User followedUser);


}
