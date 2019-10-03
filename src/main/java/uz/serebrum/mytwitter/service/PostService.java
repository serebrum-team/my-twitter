package uz.serebrum.mytwitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.serebrum.mytwitter.dao.PostDao;
import uz.serebrum.mytwitter.dao.UserDao;
import uz.serebrum.mytwitter.dao.solr.repository.PostRepository;
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
    private PostRepository postRepository;

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
        Post save = postDao.save(post);
        uz.serebrum.mytwitter.entity.solr.entity.Post solrDoc =
                new uz.serebrum.mytwitter.entity.solr.entity.Post(save.getPostId(),save.getPostTitle(),save.getOtherPostDetails().getPostBody());
        postRepository.save(solrDoc);

        return save;
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
