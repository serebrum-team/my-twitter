package uz.serebrum.mytwitter.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.serebrum.mytwitter.entity.Follower;
import uz.serebrum.mytwitter.entity.User;
import uz.serebrum.mytwitter.exception.UserNotFoundException;
import uz.serebrum.mytwitter.request.model.FollowerRequestModel;
import uz.serebrum.mytwitter.service.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/user")
public class FollowerResource {

    @Autowired
    private PrincipalService principalService;

    @Autowired
    private FollowerService followerService;

    @Autowired
    private DeleteService deleteService;

    @Autowired
    private UserService userService;

    @Autowired
    private LinkService linkService;


    @DeleteMapping("/member/{userId}")
    public ResponseEntity<Object> deleteMember(Principal principal, @PathVariable String userId) {
        User user = userService.getByUserName(principal.getName());
        if (deleteService.deleteMember(user.getUserId(), Long.parseLong(userId))) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
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


    @PostMapping(path = "/follower")
    public ResponseEntity<Object> createFollower(@RequestBody FollowerRequestModel followerRequestModel, Principal principal) {

        User user = principalService.loadByUserName(principal.getName());
        followerRequestModel.setMemberUserId(user.getUserId());
        if (!followerService.existsByUserId(followerRequestModel.getUserId()) ||
                !followerService.existsByUserId(followerRequestModel.getMemberUserId())) {
            throw new UserNotFoundException("User not found ");
        }

        Follower follower = followerService.saveFollower(followerService.convertFollowerRequestModelToFollowerEntity(followerRequestModel));

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(follower.getFollowerId()).toUri();
        return ResponseEntity.created(uri).build();

    }

    @GetMapping(path = "/member/{userId}")//oziga azo bolgan userlar qayradi
    public Long getAllMemberCountOfUser(@PathVariable String userId) {
        if (!followerService.existsByUserId(Long.parseLong(userId))) {
            throw new UserNotFoundException("User not found ");
        }
        return followerService.memberOfUser(Long.parseLong(userId)).stream().count();
    }


    @GetMapping(path = "/followed/{userId}")//ozi azo bolgan userlar qaytadi
    public Long getAllFollowedCountOfUser(@PathVariable String userId) {
        if (!followerService.existsByUserId(Long.parseLong(userId))) {
            throw new UserNotFoundException("User not found ");
        }
        return followerService.followedUsersOfUser(Long.parseLong(userId)).stream().count();
    }

    @GetMapping(path = "/members")
    public Resource<String> getAllMembersOfAuthenticatedUser(Principal principal) {
        User user = userService.loadByUserName(principal.getName());

        Resource<String> resource = new Resource<>("All members of authenticated user ");

        List<ControllerLinkBuilder> allMemberLinkByUserId = linkService.getAllMemberOfUser(user.getUserId());
        allMemberLinkByUserId.stream().forEach(controllerLinkBuilder -> {
            resource.add(controllerLinkBuilder.withRel("user-members"));
        });
        ControllerLinkBuilder self = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(UserResource.class).getUserById(user.getUserId().toString()));
        resource.add(self.withSelfRel());


        return resource;

    }

    @GetMapping(path = "/followed")
    public Resource<String> getAllFollowedUsersLinkOfAuthenticatedUser(Principal principal) {
        User user = userService.loadByUserName(principal.getName());

        Resource<String> resource = new Resource<>("All followed of authenticated user ");

        List<ControllerLinkBuilder> allFollowedLinkByUserId = linkService.getAllFollowedOfUser(user.getUserId());
        allFollowedLinkByUserId.stream().forEach(controllerLinkBuilder -> {
            resource.add(controllerLinkBuilder.withRel("user-followed-users"));
        });

        ControllerLinkBuilder self = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(UserResource.class).getUserById(user.getUserId().toString()));
        resource.add(self.withSelfRel());



        return resource;

    }

}
