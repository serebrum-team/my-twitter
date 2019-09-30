package uz.serebrum.mytwitter.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.serebrum.mytwitter.entity.Post;
import uz.serebrum.mytwitter.entity.User;
import uz.serebrum.mytwitter.exception.PostNotFoundException;
import uz.serebrum.mytwitter.exception.UserNotFoundException;
import uz.serebrum.mytwitter.request.converter.PostConverter;
import uz.serebrum.mytwitter.request.model.PostRequestModel;
import uz.serebrum.mytwitter.service.LinkService;
import uz.serebrum.mytwitter.service.PostService;
import uz.serebrum.mytwitter.service.PrincipalService;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/user")
public class PostResource {
    @Autowired
    private PostService postService;
    @Autowired
    private PostConverter postConverter;
    @Autowired
    private LinkService linkService;
    @Autowired
    private PrincipalService principalService;

    @GetMapping(path = "/post")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @PostMapping(path = "/post")
    public ResponseEntity<Object> createPost(@RequestBody PostRequestModel postRequestModel, Principal principal) {

        User user = principalService.loadByUserName(principal.getName());
        postRequestModel.setPostAuthorId(user.getUserId());
        if (!postService.existsByUserId(postRequestModel.getPostAuthorId())){
            throw new UserNotFoundException("User id is not found");
        }

        Post savePost = postService.savePost(postConverter.convertPostRequestModelToPostEntity(postRequestModel));

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savePost.getPostId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping(path = "/post/{id}")
    public Resource<Post> getPostById(@PathVariable String id) {
        Post postById = postService.getPostById(Long.parseLong(id));

        if (postById == null) {
            throw new PostNotFoundException("postId-" + id);
        }
        Resource<Post> postResource = new Resource<>(postById);
        ControllerLinkBuilder postAuthor =
                ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(UserResource.class)
                        .getUserById(postById.getPostAuthor().getUserId().toString()));


        ControllerLinkBuilder self = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(this.getClass()).getPostById(id));
        postResource.add(self.withSelfRel());

        postResource.add(postAuthor.withRel("post-author-link"));

        linkService.getAllUsersLinkByViewedPostId(postById.getPostId()).stream().forEach(controllerLinkBuilder -> {
            postResource.add(controllerLinkBuilder.withRel("all-viewed-users-of-post"));
        });

        linkService.getAllCommentLinkByPostId(postById.getPostId()).stream().forEach(controllerLinkBuilder -> {
            postResource.add(controllerLinkBuilder.withRel("all-comments-of-post"));
        });

        return postResource;

    }


    @GetMapping(path = "/user/post/{userId}")
    public Long returnAllPostsCountByUserId(@PathVariable String userId) {
        List<Long> postList = postService.getAllPostByUserId(Long.parseLong(userId));
        if (postList == null) {
            throw new UserNotFoundException("user id not found");
        }

        return postList.stream().count();
    }
}
