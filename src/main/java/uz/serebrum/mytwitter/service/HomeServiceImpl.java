package uz.serebrum.mytwitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.serebrum.mytwitter.dao.PostDao;
import uz.serebrum.mytwitter.dao.UserDao;
import uz.serebrum.mytwitter.entity.Post;
import uz.serebrum.mytwitter.entity.User;

import java.util.List;

@Service
public class HomeServiceImpl implements HomeService{
    @Autowired
    private PostDao postDao;
    @Autowired
    private UserDao userDao;

    @Override
    public List<Long> unReadPosts(Long userId) {
        return postDao.getAllUnViewedPostsListByUserId(userId);
    }

    @Override
    public List<Long> mostReadPostAtLastDay(Long userId) {
        return postDao.getHotTop3UnViewedPosts(userId);
    }

    @Override
    public User loadUserByUserName(String userName) {
        return userDao.findByUserName(userName);
    }
}
