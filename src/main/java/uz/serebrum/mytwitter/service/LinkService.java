package uz.serebrum.mytwitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;
import uz.serebrum.mytwitter.controller.rest.CommentResource;
import uz.serebrum.mytwitter.controller.rest.PostResource;
import uz.serebrum.mytwitter.controller.rest.UserResource;
import uz.serebrum.mytwitter.controller.rest.ViewedResource;
import uz.serebrum.mytwitter.entity.Comment;
import uz.serebrum.mytwitter.exception.PostNotFoundException;
import uz.serebrum.mytwitter.exception.UserNotFoundException;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LinkService {

    @Autowired
    private PostService postService;

    @Autowired
    private FollowerService followerService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ViewedService viewedService;

    public List<ControllerLinkBuilder> getAllPostLinkByPostedUserId(Long userId){

        List<Long> postList = postService.getAllPostByUserId(Long.parseLong(userId.toString()));
        if (postList == null) {
            throw new UserNotFoundException("user id not found");
        }
        return postList.stream().map(aLong -> {
           return ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(PostResource.class).getPostById(aLong.toString()));
        }).collect(Collectors.toList());
    }

    public List<ControllerLinkBuilder> getAllMemberOfUser(Long userId){
        List<Long> memberOfUser = followerService.memberOfUser(userId);
        return memberOfUser.stream().map(aLong -> {
            return ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(UserResource.class).getUserById(aLong.toString()));
        }).collect(Collectors.toList());

    }

    public List<ControllerLinkBuilder> getAllFollowedOfUser(Long userId){
        List<Long> followedUsersOfUser = followerService.followedUsersOfUser(userId);
        return followedUsersOfUser.stream().map(aLong -> {
          return   ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(UserResource.class).getUserById(aLong.toString()));
        }).collect(Collectors.toList());
    }

    public List<ControllerLinkBuilder> getAllCommentLinkByPostId(Long postId){
        List<Long> allCommentByPostId = commentService.getAllCommentByPostId(postId);

        if (allCommentByPostId == null){
            throw new PostNotFoundException("Post not found");
        }

        return allCommentByPostId.stream().map(aLong -> {
           return ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CommentResource.class).getCommentById(aLong.toString()));
        }).collect(Collectors.toList());
    }

    public List<ControllerLinkBuilder> getAllUsersLinkByViewedPostId(Long postId){
        List<Long> usersByPostId =
                viewedService.getViewedUsersByPostId(postId);

        if (usersByPostId == null) {
            throw new PostNotFoundException("post id " + postId + " not found");
        }
        return usersByPostId.stream().map(aLong -> {
             return ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(UserResource.class).getUserById(aLong.toString()));
        }).collect(Collectors.toList());
    }
}
