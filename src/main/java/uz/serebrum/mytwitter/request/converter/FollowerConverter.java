package uz.serebrum.mytwitter.request.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uz.serebrum.mytwitter.dao.PostDao;
import uz.serebrum.mytwitter.dao.UserDao;
import uz.serebrum.mytwitter.entity.Follower;
import uz.serebrum.mytwitter.entity.Viewed;
import uz.serebrum.mytwitter.request.model.FollowerRequestModel;
import uz.serebrum.mytwitter.request.model.ViewedRequestModel;
import uz.serebrum.mytwitter.service.UserService;

@Component
public class FollowerConverter {

    @Autowired
    private UserService userService;

    public Follower convertFollowerRequestModelToFollowerEntity(FollowerRequestModel followerRequestModel) {
        return new Follower(userService.getUserById(followerRequestModel.getUserId()), userService.getUserById(followerRequestModel.getMemberUserId()));
    }
}
