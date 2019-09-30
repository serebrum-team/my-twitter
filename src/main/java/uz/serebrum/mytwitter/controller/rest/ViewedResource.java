package uz.serebrum.mytwitter.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.serebrum.mytwitter.entity.Post;
import uz.serebrum.mytwitter.entity.User;
import uz.serebrum.mytwitter.entity.Viewed;
import uz.serebrum.mytwitter.exception.PostNotFoundException;
import uz.serebrum.mytwitter.exception.UserNotFoundException;
import uz.serebrum.mytwitter.request.model.ViewedRequestModel;
import uz.serebrum.mytwitter.service.PostService;
import uz.serebrum.mytwitter.service.PrincipalService;
import uz.serebrum.mytwitter.service.ViewedService;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/user")
public class ViewedResource {

    @Autowired
    private ViewedService viewedService;

    @Autowired
    private PrincipalService principalService;

    @Autowired
    private PostService postService;

    @GetMapping(path = "/viewed")
    public List<Viewed> getAllViewed() {
        return viewedService.getAllViewed();
    }

    @GetMapping(path = "/viewed/post/{postId}")
    public Long getAllViewedUsersCountByPostId(@PathVariable String postId) {
        List<Long> usersByPostId =
                viewedService.getViewedUsersByPostId(Long.parseLong(postId));

        if (usersByPostId == null) {
            throw new PostNotFoundException("post id " + postId + " not found");
        }
        return usersByPostId.stream().count();
    }

    @PostMapping(path = "/viewed")
    public ResponseEntity<Viewed> createViewed(@RequestBody ViewedRequestModel viewedRequestModel, Principal principal) {

        User user = principalService.loadByUserName(principal.getName());

        viewedRequestModel.setViewedUserId(user.getUserId());

        if (!viewedService.existsByPostId(viewedRequestModel.getPostId())) {
            throw new PostNotFoundException("Post not found");
        }
        if (!viewedService.existsByUserId(viewedRequestModel.getViewedUserId())) {
            throw new UserNotFoundException("User not found");
        }


        Viewed viewed = viewedService.convertViewedRequestModelToViewedEntity(viewedRequestModel);


        Viewed saveViewed = viewedService.saveViewed(viewed);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(saveViewed.getViewedId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/viewed/user/history/{userId}")
    public Resource<User> getUserViewedHistoryByUserId(@PathVariable String userId) {
        User user = principalService.loadByUserId(Long.parseLong(userId));

        if (user == null){
            throw new UserNotFoundException(userId+" userId not found");
        }

        List<Long> viewedHistoryByUserId = viewedService.getViewedHistoryByUserId(user.getUserId());
        Resource<User> userResource = new Resource<>(user);

        viewedHistoryByUserId.stream().forEach(aLong -> {
            userResource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(PostResource.class).getPostById(aLong.toString())).withRel("viewed-posts"));
        });

        return userResource;
    }



    @GetMapping("/viewed/post/history/{postId}")
    public Resource<Post> getPostViewedHistoryByPostId(@PathVariable String postId){

        Post postById = postService.getPostById(Long.parseLong(postId));
        if (postById == null){
            throw new PostNotFoundException(postId+" postId not found");
        }
        Resource<Post> postResource = new Resource<>(postById);

        viewedService.getViewedUsersByPostId(postById.getPostId()).stream().forEach(aLong -> {
            postResource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(UserResource.class).getUserById(aLong.toString())).withRel("viewed-users"));
        });

        return postResource;
    }

}
