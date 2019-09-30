package uz.serebrum.mytwitter.service;

import org.springframework.stereotype.Service;
import uz.serebrum.mytwitter.entity.Post;
import uz.serebrum.mytwitter.entity.User;

import java.util.List;

public interface HomeService {

     List<Long> unReadPosts(Long userId);

     List<Long> mostReadPostAtLastDay(Long userId);

     User loadUserByUserName(String userName);
}
