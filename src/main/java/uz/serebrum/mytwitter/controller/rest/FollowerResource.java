package uz.serebrum.mytwitter.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.serebrum.mytwitter.entity.Follower;
import uz.serebrum.mytwitter.entity.User;
import uz.serebrum.mytwitter.exception.UserNotFoundException;
import uz.serebrum.mytwitter.request.model.FollowerRequestModel;
import uz.serebrum.mytwitter.service.FollowerService;
import uz.serebrum.mytwitter.service.PrincipalService;

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
}
