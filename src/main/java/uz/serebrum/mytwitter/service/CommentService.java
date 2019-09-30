package uz.serebrum.mytwitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.serebrum.mytwitter.dao.CommentDao;
import uz.serebrum.mytwitter.dao.PostDao;
import uz.serebrum.mytwitter.dao.UserDao;
import uz.serebrum.mytwitter.entity.Comment;
import uz.serebrum.mytwitter.exception.PostNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private PostDao postDao;
    @Autowired
    private UserDao userDao;

    public List<Comment> getAllComments() {
        List<Comment> comments = new ArrayList<>();
        commentDao.findAll().forEach(comments::add);
        return comments;
    }

    public Comment getCommentById(Long commentId) {

        if (!commentDao.existsById(commentId))
            return null;

        return commentDao.findById(commentId).get();
    }


    public List<Long> getAllCommentByPostId(Long postId){
        if (!postDao.existsById(postId))
            return null;

        return commentDao.findAllByCommentedPost_PostId(postId);
    }
    public Comment saveComment(Comment comment){
        Long postId = comment.getCommentedPost().getPostId();
        if (!postDao.existsById(postId)){
            throw new PostNotFoundException(postId+" postId is not found");
        }
        return commentDao.save(comment);
    }

    public boolean existsByPostId(Long postId){
        return postDao.existsById(postId);
    }
    public boolean existsByUserId(Long userId){
        return userDao.existsById(userId);
    }
}
