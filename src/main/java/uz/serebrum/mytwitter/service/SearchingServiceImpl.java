package uz.serebrum.mytwitter.service;

import org.springframework.stereotype.Service;
import uz.serebrum.mytwitter.dao.PostDao;
import uz.serebrum.mytwitter.dao.UserDao;
import uz.serebrum.mytwitter.dao.solr.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchingServiceImpl implements SearchingService {

    private final UserDao userDao;
    private final PostDao postDao;

    private final PostRepository postRepository;

    public SearchingServiceImpl(UserDao userDao, PostDao postDao, PostRepository postRepository) {
        this.userDao = userDao;
        this.postDao = postDao;
        this.postRepository = postRepository;
    }

    @Override
    public boolean alreadyHasThisUserName(String userName) {
        return userDao.existsByUserName(userName);
    }

    @Override
    public List<Long> getAllMatchedPosts(String word) {
      return postRepository.searchPostsByWord(word).stream().map(post -> {
          return post.getId();
       }).collect(Collectors.toList());
    }

}
