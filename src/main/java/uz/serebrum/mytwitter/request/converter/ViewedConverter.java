package uz.serebrum.mytwitter.request.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uz.serebrum.mytwitter.dao.PostDao;
import uz.serebrum.mytwitter.dao.UserDao;
import uz.serebrum.mytwitter.dao.ViewedDao;
import uz.serebrum.mytwitter.entity.Comment;
import uz.serebrum.mytwitter.entity.Viewed;
import uz.serebrum.mytwitter.request.model.CommentRequestModel;
import uz.serebrum.mytwitter.request.model.ViewedRequestModel;

@Component
public class ViewedConverter {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PostDao postDao;

    public Viewed convertViewedRequestModelToViewedEntity(ViewedRequestModel viewedRequestModel){
        Viewed viewed = new Viewed();
        if (!userDao.existsById(viewedRequestModel.getViewedUserId()))
            return null;

        if (!postDao.existsById(viewedRequestModel.getPostId()))
            return null;
        viewed.setPost(postDao.findById(viewedRequestModel.getPostId()).get());
        viewed.setViewedUser(userDao.findById(viewedRequestModel.getViewedUserId()).get());
        return viewed;
    }
}
