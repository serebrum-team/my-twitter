package uz.serebrum.mytwitter.service;

import org.springframework.stereotype.Service;
import uz.serebrum.mytwitter.dao.CommentDao;
import uz.serebrum.mytwitter.dao.FollowerDao;
import uz.serebrum.mytwitter.dao.PostDao;
import uz.serebrum.mytwitter.dao.UserDao;
import uz.serebrum.mytwitter.dao.solr.repository.PostRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class DeleteServiceImpl implements DeleteService {
    private final UserDao userDao;
    private final PostDao postDao;
    private final CommentDao commentDao;
    private final FollowerDao followerDao;
    private final PostRepository postRepository;

    public DeleteServiceImpl(UserDao userDao, PostDao postDao, CommentDao commentDao, FollowerDao followerDao, PostRepository postRepository) {
        this.userDao = userDao;
        this.postDao = postDao;
        this.commentDao = commentDao;
        this.followerDao = followerDao;
        this.postRepository = postRepository;
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
        postRepository.deleteById(postId);

        if (!postDao.existsById(postId))
            return true;

        return false;
    }

    @Override
    public boolean deleteComment(Long userId, Long commentId) {

        if (!commentDao.existsByCommentIdAndCommentAuthor(commentId, userDao.findById(userId).get()))
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

        if (!followerDao.existsByUser_UserIdAndFollowedUser(userId, userDao.findById(memberId).get()))
            return true;
        followerDao.deleteByUser_UserIdAndFollowedUser(userId, userDao.findById(memberId).get());
        if (!followerDao.existsByUser_UserIdAndFollowedUser(userId, userDao.findById(memberId).get()))
            return true;

        return false;
    }

    @Override
    public boolean deleteMember(Long userId, Long memberId) {

        if (!followerDao.existsByUser_UserIdAndFollowedUser(userId, userDao.findById(memberId).get()))
            return true;
        followerDao.deleteByUser_UserIdAndFollowedUser(userId, userDao.findById(memberId).get());
        if (!followerDao.existsByUser_UserIdAndFollowedUser(userId, userDao.findById(memberId).get()))
            return true;

        return false;
    }
}
