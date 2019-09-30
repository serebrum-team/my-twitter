package uz.serebrum.mytwitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import uz.serebrum.mytwitter.entity.*;
import uz.serebrum.mytwitter.entity.embeddable.OtherPostDetails;
import uz.serebrum.mytwitter.entity.embeddable.OtherUserDetails;
import uz.serebrum.mytwitter.service.*;


import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class MyTwitterApplication {
    private static Logger logger = LoggerFactory.getLogger(MyTwitterApplication.class);


    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MyTwitterApplication.class, args);
        UserService userService = context.getBean(UserService.class);

        RoleService roleService = context.getBean(RoleService.class);

        roleService.saveRole(new Role("USER",new ArrayList<>()));
        List<Role> roles = new ArrayList<>();
        roles.add(roleService.getRoleById(1l));


        userService.saveUser(new User("account1","password1",roles,new OtherUserDetails("Behruz","Mahmudov","Any bio","makhmudovbekhruzish@gmail.com")));
        userService.saveUser(new User("account2","password2",roles,new OtherUserDetails("Islom","Toshev","Any bio","makhmudovbekhruzish@gmail.com")));
        userService.saveUser(new User("account3","password3",roles,new OtherUserDetails("Oybek","Muhiddinov","Any bio","makhmudovbekhruzish@gmail.com")));
        userService.saveUser(new User("account4","password4",roles,new OtherUserDetails("Eljahon","Normominov","Any bio","makhmudovbekhruzish@gmail.com")));
        userService.saveUser(new User("account5","password5",roles,new OtherUserDetails("Salim","Dushanov","Any bio","makhmudovbekhruzish@gmail.com")));


        FollowerService followerService = context.getBean(FollowerService.class);

        followerService.saveFollower(new Follower(userService.getUserById(1l),2l));
        followerService.saveFollower(new Follower(userService.getUserById(1l),3l));
        followerService.saveFollower(new Follower(userService.getUserById(2l),3l));
        followerService.saveFollower(new Follower(userService.getUserById(4l),3l));
        followerService.saveFollower(new Follower(userService.getUserById(1l),4l));
        followerService.saveFollower(new Follower(userService.getUserById(1l),5l));
        followerService.saveFollower(new Follower(userService.getUserById(2l),4l));
        followerService.saveFollower(new Follower(userService.getUserById(5l),2l));
        followerService.saveFollower(new Follower(userService.getUserById(5l),4l));
        followerService.saveFollower(new Follower(userService.getUserById(4l),2l));

        PostService postService = context.getBean(PostService.class);

        postService.savePost(new Post("post title1",userService.getUserById(1l),new OtherPostDetails("Post body 1")));
        postService.savePost(new Post("post title2",userService.getUserById(1l),new OtherPostDetails("Post body 2")));
        postService.savePost(new Post("post title3",userService.getUserById(1l),new OtherPostDetails("Post body 3")));
        postService.savePost(new Post("post title4",userService.getUserById(2l),new OtherPostDetails("Post body 4")));
        postService.savePost(new Post("post title5",userService.getUserById(2l),new OtherPostDetails("Post body 5")));
        postService.savePost(new Post("post title6",userService.getUserById(5l),new OtherPostDetails("Post body 6")));
        postService.savePost(new Post("post title7",userService.getUserById(5l),new OtherPostDetails("Post body 7")));
        postService.savePost(new Post("post title8",userService.getUserById(4l),new OtherPostDetails("Post body 8")));
        postService.savePost(new Post("post title10",userService.getUserById(3l),new OtherPostDetails("Post body 10")));
        postService.savePost(new Post("post title11",userService.getUserById(3l),new OtherPostDetails("Post body 11")));
        postService.savePost(new Post("post title12",userService.getUserById(3l),new OtherPostDetails("Post body 12")));

        ViewedService viewedService = context.getBean(ViewedService.class);
        viewedService.saveViewed(new Viewed(postService.getPostById(1l),userService.getUserById(2l)));
        viewedService.saveViewed(new Viewed(postService.getPostById(1l),userService.getUserById(3l)));
        viewedService.saveViewed(new Viewed(postService.getPostById(1l),userService.getUserById(4l)));
        viewedService.saveViewed(new Viewed(postService.getPostById(2l),userService.getUserById(1l)));
        viewedService.saveViewed(new Viewed(postService.getPostById(2l),userService.getUserById(2l)));
        viewedService.saveViewed(new Viewed(postService.getPostById(3l),userService.getUserById(5l)));
        viewedService.saveViewed(new Viewed(postService.getPostById(3l),userService.getUserById(3l)));
        viewedService.saveViewed(new Viewed(postService.getPostById(3l),userService.getUserById(4l)));
        viewedService.saveViewed(new Viewed(postService.getPostById(5l),userService.getUserById(1l)));
        viewedService.saveViewed(new Viewed(postService.getPostById(6l),userService.getUserById(3l)));
        viewedService.saveViewed(new Viewed(postService.getPostById(6l),userService.getUserById(2l)));
        viewedService.saveViewed(new Viewed(postService.getPostById(3l),userService.getUserById(2l)));

        CommentService commentService = context.getBean(CommentService.class);
        commentService.saveComment(new Comment("comment body 1",postService.getPostById(1L),1l));
        commentService.saveComment(new Comment("comment body 2",postService.getPostById(1L),2l));
        commentService.saveComment(new Comment("comment body 3",postService.getPostById(2L),3l));
        commentService.saveComment(new Comment("comment body 4",postService.getPostById(2L),4l));
        commentService.saveComment(new Comment("comment body 5",postService.getPostById(2L),5l));
        commentService.saveComment(new Comment("comment body 6",postService.getPostById(3L),4l));
        commentService.saveComment(new Comment("comment body 7",postService.getPostById(3L),2l));
        commentService.saveComment(new Comment("comment body 8",postService.getPostById(4l),1l));
        commentService.saveComment(new Comment("comment body 9",postService.getPostById(4l),3l));
        commentService.saveComment(new Comment("comment body 0",postService.getPostById(4l),4l));
        commentService.saveComment(new Comment("comment body 10",postService.getPostById(5l),4l));
        commentService.saveComment(new Comment("comment body 11",postService.getPostById(6l),4l));
        commentService.saveComment(new Comment("comment body 12",postService.getPostById(7l),4l));
        commentService.saveComment(new Comment("comment body 13",postService.getPostById(8l),4l));


    }
}
