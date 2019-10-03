package uz.serebrum.mytwitter.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.serebrum.mytwitter.entity.Comment;
import uz.serebrum.mytwitter.entity.User;
import uz.serebrum.mytwitter.exception.CommentNotFoundException;
import uz.serebrum.mytwitter.exception.PostNotFoundException;
import uz.serebrum.mytwitter.exception.UserNotFoundException;
import uz.serebrum.mytwitter.request.converter.CommentConverter;
import uz.serebrum.mytwitter.request.model.CommentRequestModel;
import uz.serebrum.mytwitter.service.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/user")
public class CommentResource {

    private final PrincipalService principalService;

    private final CommentService commentService;
    private final CommentConverter commentConverter;


    private final DeleteService deleteService;

    private final UserService userService;

    private final UpdateService updateService;

    public CommentResource(PrincipalService principalService, CommentService commentService, CommentConverter commentConverter, DeleteService deleteService, UserService userService, UpdateService updateService) {
        this.principalService = principalService;
        this.commentService = commentService;
        this.commentConverter = commentConverter;
        this.deleteService = deleteService;
        this.userService = userService;
        this.updateService = updateService;
    }

    @PutMapping("/comment/{commentId}")
    public ResponseEntity<Comment> updateComment(Principal principal,
                                                 @RequestBody CommentRequestModel commentRequestModel,
                                                 @PathVariable String commentId){
        User user = userService.getByUserName(principal.getName());

        System.out.println("UpdateController.updateComment");
        Comment updateComment = updateService.updateComment(user.getUserId(), Long.parseLong(commentId), commentRequestModel);

        if (updateComment == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(updateComment,HttpStatus.OK);
    }


    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Object> deleteComment(Principal principal, @PathVariable String commentId) {

        User user = userService.getByUserName(principal.getName());

        if (deleteService.deleteComment(user.getUserId(), Long.parseLong(commentId)))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);

    }



    @GetMapping(path = "/comment/{id}")
    public Resource<Comment> getCommentById(@PathVariable String id) {
        Comment comment = commentService.getCommentById(Long.parseLong(id));

        if (comment == null) {
            throw new CommentNotFoundException("comment id - " + id + " not found");
        }


        ControllerLinkBuilder commentAuthorLink = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(UserResource.class).
                getUserById(comment.getCommentAuthorId().toString()));


        ControllerLinkBuilder commentPostLink = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(PostResource.class)
                .getPostById(comment.getCommentedPost().getPostId().toString()));
        ControllerLinkBuilder self = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(this.getClass()).getCommentById(id));

        Resource<Comment> commentResource = new Resource<>(comment);

        commentResource.add(commentAuthorLink.withRel("comment-author-link"));
        commentResource.add(commentPostLink.withRel("commented-post-link"));
        commentResource.add(self.withSelfRel());

        return commentResource;
    }


    @PostMapping(path = "/comment")
    public ResponseEntity<Object> createComment(@RequestBody CommentRequestModel commentRequestModel, Principal principal) {

        User user = principalService.loadByUserName(principal.getName());
        commentRequestModel.setCommentAuthorId(user.getUserId());
        if (!commentService.existsByPostId(commentRequestModel.getCommentedPostId())) {
            throw new PostNotFoundException("Post id not found");
        }
        if (!commentService.existsByUserId(commentRequestModel.getCommentAuthorId())) {
            throw new UserNotFoundException("User id not found");
        }

        Comment comment = commentService.saveComment(commentConverter.convertCommentRequestModelToCommentEntity(commentRequestModel));


        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(comment.getCommentId()).toUri();

        return ResponseEntity.created(uri).build();
    }


    @GetMapping("/post/comment/{postId}")
    public Long returnAllCommentCountByPostId(@PathVariable String postId) {

        if (!commentService.existsByPostId(Long.parseLong(postId))) {
            throw new PostNotFoundException("Post id not found");
        }

        List<Long> allCommentByPostId = commentService.getAllCommentByPostId(Long.parseLong(postId));

        if (allCommentByPostId == null) {
            throw new PostNotFoundException("Post not found");
        }


        return allCommentByPostId.stream().count();
    }

}
