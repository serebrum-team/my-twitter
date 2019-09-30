package uz.serebrum.mytwitter.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.serebrum.mytwitter.entity.User;
import uz.serebrum.mytwitter.exception.UserNotFoundException;
import uz.serebrum.mytwitter.request.converter.UserConverter;
import uz.serebrum.mytwitter.request.model.UserRequestModel;
import uz.serebrum.mytwitter.request.model.UserRequestModelWithSecretCode;
import uz.serebrum.mytwitter.service.EmailService;
import uz.serebrum.mytwitter.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private EmailService emailService;


    @PostMapping(path = "/register")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserRequestModel userRequestModel) {

        Long secretCodeFromServer = EmailService.emailsAndSecretCode.get(userRequestModel.getEmail());
        Long secretCodeFromClient = userRequestModel.getSecretCode();

        System.out.println("secretCodeFromClient = " + secretCodeFromClient);
        System.out.println("secretCodeFromServer = " + secretCodeFromServer);

        if (!secretCodeFromClient.equals(secretCodeFromServer))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    try {
        User saveUser = userService.saveUser(userConverter.convertRequestModelToUserEntity(userRequestModel));
        if (saveUser == null) {
            throw new UserNotFoundException("Username already has");
        }
    }catch (Exception e){
        e.printStackTrace();
    }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(path = "/send-email-address")
    public ResponseEntity<Object> sendEmailAndSecretCode(@RequestBody UserRequestModelWithSecretCode modelWithSecretCode) {
        emailService.sendSecretCode(modelWithSecretCode.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
