package uz.serebrum.mytwitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.serebrum.mytwitter.dao.PostDao;
import uz.serebrum.mytwitter.dao.UserDao;
import uz.serebrum.mytwitter.entity.Post;
import uz.serebrum.mytwitter.entity.User;
import uz.serebrum.mytwitter.entity.embeddable.OtherPostDetails;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostDao postDao;

    @Autowired
    private UserDao userDao;
    public List<Post> getAllPosts(){
        List<Post> posts = new ArrayList<>();
        postDao.findAll().forEach(posts::add);
        return posts;
    }

    public Post savePost(Post post){
        post.setPostAuthor(userDao.findById(post.getPostAuthor().getUserId()).get());
        post.setPostComments(new ArrayList<>());
        post.setPostVieweds(new ArrayList<>());
        return postDao.save(post);
    }

    public Post getPostById(Long id){
        if (!postDao.existsById(id)){
            return null;
        }

        return postDao.findById(id).get();
    }

    public List<Long> getAllPostByUserId(Long userId){
        if (!userDao.existsById(userId)){
            return null;
        }
        return postDao.getAllByPostAuthor_UserId(userId);
    }

    public boolean existsByUserId(Long userId){
        return userDao.existsById(userId);
    }

    public boolean existsByPostId(Long postId){
        return postDao.existsById(postId);
    }
}
