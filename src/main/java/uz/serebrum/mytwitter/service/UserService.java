package uz.serebrum.mytwitter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.serebrum.mytwitter.dao.RoleDao;
import uz.serebrum.mytwitter.dao.UserDao;
import uz.serebrum.mytwitter.entity.Role;
import uz.serebrum.mytwitter.entity.User;
import uz.serebrum.mytwitter.entity.embeddable.OtherUserDetails;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService, PrincipalService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    public List<User> getAllUsers() {
        List users = new ArrayList();
        userDao.findAll().forEach(users::add);
        return users;
    }

    public User getUserById(Long id) {

        if (!userDao.existsById(id))
            return null;
        User user = userDao.findById(id).get();

        return user;
    }

    public User saveUser(User user) {

        if (userDao.existsByUserName(user.getUsername()))
            return null;
        user.setUserPosts(new ArrayList<>());
        user.setUserFollowers(new ArrayList<>());
        user.setUserVieweds(new ArrayList<>());
        List<Role> roles = new ArrayList<>();
        roles.add(roleDao.findByRoleName("USER"));
        user.setRoles(roles);
        return userDao.save(user);
    }

    public boolean existsByUserId(Long userId) {
        return userDao.existsById(userId);
    }

    public boolean isUser(String username, String password) {
        return userDao.existsByUserNameAndPassword(username, password);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.findByUserName(username);
    }

    public User getByUserName(String username) {
        return userDao.findByUserName(username);
    }

    @Override
    public User loadByUserName(String userName) {
        return userDao.findByUserName(userName);
    }

    @Override
    public User loadByUserId(Long userId) {
        return userDao.findById(userId).get();
    }
}
