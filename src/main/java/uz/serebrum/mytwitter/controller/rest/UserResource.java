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
import uz.serebrum.mytwitter.service.LinkService;
import uz.serebrum.mytwitter.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
public class UserResource {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserService userService;

    @Autowired
    private LinkService linkService;

    @GetMapping(path = "/user")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
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


}
