package uz.serebrum.mytwitter.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.serebrum.mytwitter.entity.Comment;
import uz.serebrum.mytwitter.entity.Post;
import uz.serebrum.mytwitter.entity.User;
import uz.serebrum.mytwitter.request.model.CommentRequestModel;
import uz.serebrum.mytwitter.request.model.PostRequestModel;
import uz.serebrum.mytwitter.request.model.UserRequestModel;
import uz.serebrum.mytwitter.service.PrincipalService;
import uz.serebrum.mytwitter.service.UpdateService;
import uz.serebrum.mytwitter.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user/update")
public class UpdateController {

    private final UserService userService;
    private final UpdateService updateService;

    public UpdateController(UserService userService, UpdateService updateService) {
        this.userService = userService;
        this.updateService = updateService;
    }

    @PutMapping("/account")
    public User updateUser(Principal principal, @RequestBody UserRequestModel userRequestModel){

        User  user = userService.getByUserName(principal.getName());

        User updateUserAccount = updateService.updateUserAccount(user.getUserId(), userRequestModel);
        return updateUserAccount;
    }

    @PutMapping("/post/{postId}")
    public ResponseEntity<Post> updatePost(Principal principal, @PathVariable String postId, @RequestBody PostRequestModel postRequestModel){
        User user = userService.getByUserName(principal.getName());
        Post updatePost = updateService.updatePost(user.getUserId(), Long.parseLong(postId), postRequestModel);
        if (updatePost == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(updatePost,HttpStatus.OK);
    }

    @PutMapping("/comment/{commentId}")
    public ResponseEntity<Comment> updateComment(Principal principal, @RequestBody CommentRequestModel commentRequestModel, @PathVariable String commentId){
        User user = userService.getByUserName(principal.getName());

        System.out.println("UpdateController.updateComment");
        Comment updateComment = updateService.updateComment(user.getUserId(), Long.parseLong(commentId), commentRequestModel);

        if (updateComment == null)
             return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
         return new ResponseEntity<>(updateComment,HttpStatus.OK);
    }

}
