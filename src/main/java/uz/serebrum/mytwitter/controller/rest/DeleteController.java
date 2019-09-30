package uz.serebrum.mytwitter.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.serebrum.mytwitter.entity.User;
import uz.serebrum.mytwitter.service.DeleteService;
import uz.serebrum.mytwitter.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user/delete")
public class DeleteController {

    @Autowired
    private DeleteService deleteService;

    @Autowired
    private UserService userService;

    @DeleteMapping("/account")
    public ResponseEntity<Object> deleteAccount(Principal principal) {
        User user = userService.getByUserName(principal.getName());

        boolean deleteUserAccount = deleteService.deleteUserAccount(user.getUserId());
        if (deleteUserAccount)
            return new ResponseEntity<Object>(HttpStatus.OK);

        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Object> deletePost(Principal principal, @PathVariable String postId) {

        User user = userService.getByUserName(principal.getName());

        if (deleteService.deletePost(user.getUserId(), Long.parseLong(postId)))
            return new ResponseEntity<Object>(HttpStatus.OK);
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Object> deleteComment(Principal principal, @PathVariable String commentId) {

        User user = userService.getByUserName(principal.getName());

        if (deleteService.deleteComment(user.getUserId(), Long.parseLong(commentId)))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);

    }


    @DeleteMapping("/follower/{userId}")
    public ResponseEntity<Object> deleteFollowing(Principal principal, @PathVariable String userId) {

        User user = userService.getByUserName(principal.getName());
        if (deleteService.deleteFollowed(Long.parseLong(userId), user.getUserId())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/member/{userId}")
    public ResponseEntity<Object> deleteMember(Principal principal, @PathVariable String userId) {
        User user = userService.getByUserName(principal.getName());
        if (deleteService.deleteMember(user.getUserId(), Long.parseLong(userId))) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
