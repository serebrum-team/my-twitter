package uz.serebrum.mytwitter.request.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uz.serebrum.mytwitter.dao.PostDao;
import uz.serebrum.mytwitter.dao.UserDao;
import uz.serebrum.mytwitter.entity.Post;
import uz.serebrum.mytwitter.entity.embeddable.OtherPostDetails;
import uz.serebrum.mytwitter.request.model.PostRequestModel;

@Component
public class PostConverter {

    private final UserDao userDao;

    public PostConverter(UserDao userDao) {
        this.userDao = userDao;
    }

    public Post convertPostRequestModelToPostEntity(PostRequestModel postRequestModel){

        Post post = new Post();
        post.setPostAuthor(userDao.findById(postRequestModel.getPostAuthorId()).get());
        post.setPostTitle(postRequestModel.getPostTitle());
        post.setOtherPostDetails(postRequestModel.getOtherPostDetails());

        return post;
        
    }
}
