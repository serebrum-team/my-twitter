package uz.serebrum.mytwitter.service;

import org.springframework.stereotype.Service;
import uz.serebrum.mytwitter.dao.PostDao;
import uz.serebrum.mytwitter.dao.UserDao;

import java.util.List;

@Service
public class SearchingServiceImpl implements SearchingService {

    private final UserDao userDao;
    private final PostDao postDao;

    public SearchingServiceImpl(UserDao userDao, PostDao postDao) {
        this.userDao = userDao;
        this.postDao = postDao;
    }

    @Override
    public boolean alreadyHasThisUserName(String userName) {
        return userDao.existsByUserName(userName);
    }

}
