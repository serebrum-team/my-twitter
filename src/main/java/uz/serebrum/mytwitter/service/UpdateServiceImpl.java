package uz.serebrum.mytwitter.service;

import org.springframework.stereotype.Service;
import uz.serebrum.mytwitter.dao.CommentDao;
import uz.serebrum.mytwitter.dao.FollowerDao;
import uz.serebrum.mytwitter.dao.PostDao;
import uz.serebrum.mytwitter.dao.UserDao;
import uz.serebrum.mytwitter.entity.Comment;
import uz.serebrum.mytwitter.entity.Post;
import uz.serebrum.mytwitter.entity.User;
import uz.serebrum.mytwitter.request.model.CommentRequestModel;
import uz.serebrum.mytwitter.request.model.PostRequestModel;
import uz.serebrum.mytwitter.request.model.UserRequestModel;

@Service
public class UpdateServiceImpl implements UpdateService {

    private final UserDao userDao;
    private final PostDao postDao;
    private final CommentDao commentDao;
    private final FollowerDao followerDao;
    private final DeleteService deleteService;

    public UpdateServiceImpl(UserDao userDao, PostDao postDao, CommentDao commentDao, FollowerDao followerDao, DeleteService deleteService) {
        this.userDao = userDao;
        this.postDao = postDao;
        this.commentDao = commentDao;
        this.followerDao = followerDao;
        this.deleteService = deleteService;
    }

    @Override
    public User updateUserAccount(Long userId, UserRequestModel userRequestModel) {
        User user = userDao.findById(userId).get();
        user.setPassword(userRequestModel.getPassword());
        user.setUserName(userRequestModel.getUserName());

        user.getOtherUserDetails().setFirstName(userRequestModel.getFirstName());
        user.getOtherUserDetails().setEmail(userRequestModel.getEmail());
        user.getOtherUserDetails().setBio(userRequestModel.getBio());
        user.getOtherUserDetails().setLastName(userRequestModel.getLastName());
        return userDao.save(user);
    }

    @Override
    public Post updatePost(Long userId,Long postId, PostRequestModel postRequestModel) {
        boolean existsByPostAuthor_userIdAndPostId = postDao.existsByPostAuthor_UserIdAndPostId(userId, postId);
        if (!existsByPostAuthor_userIdAndPostId)
            return null;

        Post post = postDao.findById(postId).get();
        post.setPostTitle(postRequestModel.getPostTitle());
        post.setOtherPostDetails(postRequestModel.getOtherPostDetails());

        return postDao.save(post);
    }

    @Override
    public Comment updateComment(Long userId,Long commentId,CommentRequestModel commentRequestModel) {

        if (!commentDao.existsByCommentIdAndCommentAuthorId(commentId,userId))
            return null;

        Comment comment = commentDao.findById(commentId).get();
        comment.setCommentBody(commentRequestModel.getCommentBody());
        comment.setCommentedPost(postDao.findById(commentRequestModel.getCommentedPostId()).get());

        return commentDao.save(comment);
    }
}
