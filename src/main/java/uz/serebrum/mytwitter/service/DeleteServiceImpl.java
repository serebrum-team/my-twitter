package uz.serebrum.mytwitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.serebrum.mytwitter.dao.CommentDao;
import uz.serebrum.mytwitter.dao.FollowerDao;
import uz.serebrum.mytwitter.dao.PostDao;
import uz.serebrum.mytwitter.dao.UserDao;
import uz.serebrum.mytwitter.entity.Follower;

import javax.transaction.Transactional;

@Service
@Transactional
public class DeleteServiceImpl implements DeleteService {
    private final UserDao userDao;
    private final PostDao postDao;
    private final CommentDao commentDao;
    private final FollowerDao followerDao;

    public DeleteServiceImpl(UserDao userDao, PostDao postDao, CommentDao commentDao, FollowerDao followerDao) {
        this.userDao = userDao;
        this.postDao = postDao;
        this.commentDao = commentDao;
        this.followerDao = followerDao;
    }



    @Override
    public boolean deleteUserAccount(Long userId) {

        if (!userDao.existsById(userId))
            return true;

        userDao.deleteByUserId(userId);

        if (!userDao.existsById(userId))
            return true;

        return false;
    }

    @Override
    public boolean deletePost(Long userId, Long postId) {

        if (!postDao.existsByPostAuthor_UserIdAndPostId(userId, postId))
            return false;

        if (!postDao.existsById(postId))
            return true;

        postDao.deleteById(postId);

        if (!postDao.existsById(postId))
            return true;

        return false;
    }

    @Override
    public boolean deleteComment(Long userId, Long commentId) {

        if (!commentDao.existsByCommentIdAndCommentAuthorId(commentId, userId))
            return false;

        if (!commentDao.existsById(commentId))
            return true;

        commentDao.deleteById(commentId);

        if (!commentDao.existsById(commentId))
            return true;


        return false;
    }

    @Override
    public boolean deleteFollowed(Long userId, Long memberId) {

        if (!followerDao.existsByUser_UserIdAndFollowedUserId(userId, memberId))
            return true;
        followerDao.deleteByUser_UserIdAndFollowedUserId(userId, memberId);
        if (!followerDao.existsByUser_UserIdAndFollowedUserId(userId, memberId))
            return true;

        return false;
    }

    @Override
    public boolean deleteMember(Long userId, Long memberId) {

        if (!followerDao.existsByUser_UserIdAndFollowedUserId(userId, memberId))
            return true;
        followerDao.deleteByUser_UserIdAndFollowedUserId(userId, memberId);
        if (!followerDao.existsByUser_UserIdAndFollowedUserId(userId, memberId))
            return true;

        return false;
    }
}
