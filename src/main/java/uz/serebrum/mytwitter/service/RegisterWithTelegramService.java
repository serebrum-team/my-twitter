package uz.serebrum.mytwitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.serebrum.mytwitter.dao.UserDao;

@Service
public class RegisterWithTelegramService {

    @Autowired
    private UserDao userDao;
    public boolean existsUser(Long userId){
        return userDao.existsById(userId);
    }

    public boolean existsUserByUserName(String username){
        return userDao.existsByUserName(username);
    }
}
