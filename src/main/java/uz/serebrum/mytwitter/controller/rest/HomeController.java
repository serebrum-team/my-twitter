package uz.serebrum.mytwitter.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.serebrum.mytwitter.entity.Post;
import uz.serebrum.mytwitter.entity.User;
import uz.serebrum.mytwitter.service.HomeService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping("/user/home")
    public Resource<User> homePageData(Principal principal){
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
}
