package uz.serebrum.mytwitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import uz.serebrum.mytwitter.dao.FollowerDao;
import uz.serebrum.mytwitter.dao.UserDao;
import uz.serebrum.mytwitter.entity.Follower;
import uz.serebrum.mytwitter.entity.User;
import uz.serebrum.mytwitter.request.converter.FollowerConverter;

import javax.security.sasl.Sasl;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class FollowerService extends FollowerConverter {

    @Autowired
    private FollowerDao followerDao;
    @Autowired
    private UserDao userDao;

    //ushbu metod ozi ga azo bolgan userlar listini qaytaradi
    public List<Long> memberOfUser(Long userId) {
        return userDao.getAllUserMembers(userId);

    }

    //ushbu metod ozi azo bolgan userlar listini qaytaradi
    public List<Long> followedUsersOfUser(Long userId) {

        return userDao.getAllUserFollowers(userId);

    }

    public boolean existsByUserId(Long userId){
        return userDao.existsById(userId);
    }


    public Follower saveFollower(Follower follower) {
        return followerDao.save(follower);
    }
}
