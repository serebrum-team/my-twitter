package uz.serebrum.mytwitter.service;

import org.aspectj.weaver.ast.Literal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.serebrum.mytwitter.dao.PostDao;
import uz.serebrum.mytwitter.dao.UserDao;
import uz.serebrum.mytwitter.dao.ViewedDao;
import uz.serebrum.mytwitter.entity.User;
import uz.serebrum.mytwitter.entity.Viewed;
import uz.serebrum.mytwitter.request.converter.ViewedConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ViewedService extends ViewedConverter {

    @Autowired
    private ViewedDao viewedDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PostDao postDao;


    public List<Viewed> getAllViewed() {
        List<Viewed> vieweds = new ArrayList<>();
        viewedDao.findAll().forEach(vieweds::add);
        return vieweds;
    }

    //viewed obyekti saqlab qoyiladi
    public Viewed saveViewed(Viewed viewed) {
        return viewedDao.save(viewed);
    }

    //bu metod yuborilgan post id dagi postni korgan userlar listini qaytaradi
    public List<Long> getViewedUsersByPostId(Long postId) {

        if (!postDao.existsById(postId))
            return null;

        return userDao.getAllUsersByViewedPostId(postId);

    }

    //bu metod yuborilgan post id dagi postni korgan userlar sonini qaytaradi
    public Long getViewedUserCountsByPostId(Long postId) {

        if (!postDao.existsById(postId))
            return null;

        return viewedDao.countViewedUsersByPostId(postId);

    }


    public List<Long> getViewedHistoryByUserId(Long userId) {
        return viewedDao.findAllByViewedUser_UserId(userId).stream().map(viewed -> {
            return viewed.getPost().getPostId();
        }).collect(Collectors.toList());
    }

    public List<Long> getViewedHistoryByPostId(Long postId){
        return viewedDao.findAllByPost_PostId(postId).stream().map(viewed -> {
            return viewed.getViewedUser().getUserId();
        }).collect(Collectors.toList());
    }

    public boolean existsByPostId(Long postId) {
        return postDao.existsById(postId);
    }

    public boolean existsByUserId(Long userId) {
        return userDao.existsById(userId);
    }
}
