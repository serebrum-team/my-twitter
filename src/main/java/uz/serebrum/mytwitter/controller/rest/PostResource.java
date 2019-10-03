package uz.serebrum.mytwitter.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.serebrum.mytwitter.entity.Post;
import uz.serebrum.mytwitter.entity.User;
import uz.serebrum.mytwitter.exception.PostNotFoundException;
import uz.serebrum.mytwitter.exception.UserNotFoundException;
import uz.serebrum.mytwitter.request.converter.PostConverter;
import uz.serebrum.mytwitter.request.model.PostRequestModel;
import uz.serebrum.mytwitter.service.*;

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


    @Autowired
    private DeleteService deleteService;

    @Autowired
    private UserService userService;

    @Autowired
    private  UpdateService updateService;

    @Autowired
    private SearchingService searchingService;

    @GetMapping(path = "/post/search/{searchingWord}")
    public Resource<String> getAllSearchResultsWithLinks(@PathVariable String searchingWord){
        Resource<String> resource = new Resource<>("All matched post links");
        searchingService.getAllMatchedPosts(searchingWord).stream().forEach(aLong -> {
            resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(PostResource.class).getPostById(aLong.toString())).withRel("all matched posts link"));
        });
        return resource;
    }


    @GetMapping("/posts")
    public Resource<String> getAllUserPostsURLOfAuthenticatedUser(Principal principal){

        User user = userService.loadByUserName(principal.getName());
        Resource<String> resource = new Resource<>("All user posts link");
        List<ControllerLinkBuilder> allPostLinkByPostedUserId = linkService.getAllPostLinkByPostedUserId(user.getUserId());
        allPostLinkByPostedUserId.stream().forEach(controllerLinkBuilder -> {
            resource.add(controllerLinkBuilder.withRel("user-posts"));
        });

        return resource;
    }

    @PutMapping("/post/{postId}")
    public ResponseEntity<Post> updatePost(Principal principal, @PathVariable String postId, @RequestBody PostRequestModel postRequestModel){
        User user = userService.getByUserName(principal.getName());
        Post updatePost = updateService.updatePost(user.getUserId(), Long.parseLong(postId), postRequestModel);
        if (updatePost == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(updatePost,HttpStatus.OK);
    }


    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Object> deletePost(Principal principal, @PathVariable String postId) {

        User user = userService.getByUserName(principal.getName());

        if (deleteService.deletePost(user.getUserId(), Long.parseLong(postId)))
            return new ResponseEntity<Object>(HttpStatus.OK);
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
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

    @GetMapping(path = "/user/post/{postId}/comments")
    public Resource<String> getAllCommentsLinkOfPost(@PathVariable String postId){
        Resource<String> resource = new Resource<>("All comments link of post");

        Post post = postService.getPostById(Long.parseLong(postId));
        if (post == null){
            throw new PostNotFoundException(postId +" postId not found ");
        }
        linkService.getAllCommentLinkByPostId(post.getPostId()).stream().forEach(controllerLinkBuilder -> {
            resource.add(controllerLinkBuilder.withRel("all-comments-of-post"));
        });


        ControllerLinkBuilder self = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(this.getClass()).getPostById(postId));
        resource.add(self.withSelfRel());


        return resource;
    }

    @GetMapping(path = "/user/post/{postId}/viewedUsers")
    public Resource<String> getAllViewedUsersLinkOfPost(@PathVariable String postId){
        Resource<String> resource = new Resource<>("All viewed users link of post");

        Post post = postService.getPostById(Long.parseLong(postId));
        if (post == null){
            throw new PostNotFoundException(postId +" postId not found ");
        }
        linkService.getAllUsersLinkByViewedPostId(post.getPostId()).stream().forEach(controllerLinkBuilder -> {
            resource.add(controllerLinkBuilder.withRel("all-viewed-users-of-post"));
        });

        ControllerLinkBuilder self = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(this.getClass()).getPostById(postId));
        resource.add(self.withSelfRel());


        return resource;
    }
}
