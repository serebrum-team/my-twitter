package uz.serebrum.mytwitter.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.serebrum.mytwitter.entity.User;
import uz.serebrum.mytwitter.exception.UserNotFoundException;
import uz.serebrum.mytwitter.request.converter.UserConverter;
import uz.serebrum.mytwitter.request.model.UserRequestModel;
import uz.serebrum.mytwitter.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1")
public class UserResource {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserService userService;

    @Autowired
    private LinkService linkService;


    @Autowired
    private DeleteService deleteService;

    @Autowired
    private UpdateService updateService;

    @Autowired
    private HomeService homeService;


    @PutMapping("/account")
    public User updateUser(Principal principal, @RequestBody UserRequestModel userRequestModel){

        User  user = userService.getByUserName(principal.getName());

        User updateUserAccount = updateService.updateUserAccount(user.getUserId(), userRequestModel);
        return updateUserAccount;
    }


    @Autowired
    private SearchingService searchingService;

    @GetMapping("/search/account/{word}")
    public boolean alreadyHasThisUserName(@PathVariable String word){
        return searchingService.alreadyHasThisUserName(word);
    }



    @GetMapping("/user/home")
    public Resource<User> getHomeDataOfAuthenticatedUser(Principal principal){
        User user = homeService.loadUserByUserName(principal.getName());


        List<ControllerLinkBuilder> unReadLists = homeService.unReadPosts(user.getUserId()).stream().map(aLong -> {
            return ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(PostResource.class).getPostById(aLong.toString()));
        }).collect(Collectors.toList());

        List<ControllerLinkBuilder> mostViewedPosts = homeService.mostReadPostAtLastDay(user.getUserId()).stream().map(aLong -> {
            return ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(PostResource.class).getPostById(aLong.toString()));
        }).collect(Collectors.toList());

        Resource<User> userResource = new Resource<>(user);

        unReadLists.stream().forEach(controllerLinkBuilder -> {
            userResource.add(controllerLinkBuilder.withRel("all-unread-posts-from-followed-users"));
        });

        mostViewedPosts.stream().forEach(controllerLinkBuilder -> {
            userResource.add(controllerLinkBuilder.withRel("all-unread-and-most-popular-posts"));
        });

        return userResource;
    }


    @GetMapping(path = "/user/{id}")
    public Resource<User> getUserById(@PathVariable String id) {
        User user = userService.getUserById(Long.parseLong(id));

        if (user == null) {
            throw new UserNotFoundException("User id not found");
        }

        Resource<User> userResource = new Resource<>(user);


        List<ControllerLinkBuilder> allPostLinkByPostedUserId = linkService.getAllPostLinkByPostedUserId(user.getUserId());
        allPostLinkByPostedUserId.stream().forEach(controllerLinkBuilder -> {
            userResource.add(controllerLinkBuilder.withRel("user-posts"));
        });


        List<ControllerLinkBuilder> allMemberLinkByUserId = linkService.getAllMemberOfUser(user.getUserId());
        allMemberLinkByUserId.stream().forEach(controllerLinkBuilder -> {
            userResource.add(controllerLinkBuilder.withRel("user-members"));
        });


        List<ControllerLinkBuilder> allFollowedLinkByUserId = linkService.getAllFollowedOfUser(user.getUserId());
        allFollowedLinkByUserId.stream().forEach(controllerLinkBuilder -> {
            userResource.add(controllerLinkBuilder.withRel("user-followed-users"));
        });
        ControllerLinkBuilder self = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(this.getClass()).getUserById(id));
        userResource.add(self.withSelfRel());


        return userResource;
    }


    @DeleteMapping("/account")
    public ResponseEntity<Object> deleteAccount(Principal principal) {
        User user = userService.getByUserName(principal.getName());

        boolean deleteUserAccount = deleteService.deleteUserAccount(user.getUserId());
        if (deleteUserAccount)
            return new ResponseEntity<Object>(HttpStatus.OK);

        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }


}
