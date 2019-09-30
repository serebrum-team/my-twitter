package uz.serebrum.mytwitter.request.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uz.serebrum.mytwitter.dao.PostDao;
import uz.serebrum.mytwitter.dao.UserDao;
import uz.serebrum.mytwitter.entity.Comment;
import uz.serebrum.mytwitter.request.model.CommentRequestModel;

@Component
public class CommentConverter {
    @Autowired
    private PostDao postDao;
    public Comment convertCommentRequestModelToCommentEntity(CommentRequestModel commentRequestModel){
        Comment comment = new Comment();
        comment.setCommentAuthorId(commentRequestModel.getCommentAuthorId());
        comment.setCommentBody(commentRequestModel.getCommentBody());
        comment.setCommentedPost(postDao.findById(commentRequestModel.getCommentedPostId()).get());
        return comment;
    }
}
