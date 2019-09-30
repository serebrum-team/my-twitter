package uz.serebrum.mytwitter.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.serebrum.mytwitter.service.SearchingService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/search")
public class SearchingController {

    @Autowired
    private SearchingService searchingService;

    @GetMapping("/account/{word}")
    public boolean alreadyHasThisUserName(@PathVariable String word){
        return searchingService.alreadyHasThisUserName(word);
    }


}
