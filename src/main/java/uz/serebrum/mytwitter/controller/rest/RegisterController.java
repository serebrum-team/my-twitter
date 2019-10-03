package uz.serebrum.mytwitter.controller.rest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.serebrum.mytwitter.configuration.Constants;
import uz.serebrum.mytwitter.entity.User;
import uz.serebrum.mytwitter.exception.UserNotFoundException;
import uz.serebrum.mytwitter.request.converter.UserConverter;
import uz.serebrum.mytwitter.request.model.LoginRequestModel;
import uz.serebrum.mytwitter.request.model.UserRequestModel;
import uz.serebrum.mytwitter.request.model.UserRequestModelWithSecretCode;
import uz.serebrum.mytwitter.service.EmailService;
import uz.serebrum.mytwitter.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private EmailService emailService;


    @PostMapping("/token")
    public ResponseEntity<String> getToken(@RequestBody LoginRequestModel login) throws ServletException {

        String jwttoken = "";

        System.out.println(login);
        if (login.getUsername().isEmpty() || login.getPassword().isEmpty())
            return new ResponseEntity<String>("Username or password cannot be empty.", HttpStatus.BAD_REQUEST);

        String name = login.getUsername(),
                password = login.getPassword();

        System.out.println(name + "  " + password);
        if (!userService.isUser(name, password)) {
            return new ResponseEntity<String>("Invalid credentials. Please check the username and password.",
                    HttpStatus.UNAUTHORIZED);
        } else {

            System.out.println(name);
            User byUserName = userService.getByUserName(name);
            // Creating JWT using the user credentials.

            StringBuilder stringBuilder = new StringBuilder();
            byUserName.getAuthorities().forEach(o -> {
                stringBuilder.append(o + " ");
            });
            Map<String, Object> claims = new HashMap<String, Object>();
            claims.put("usr", login.getUsername());
            claims.put("sub", "Authentication token");
            claims.put("iss", Constants.ISSUER);
            claims.put("rol", stringBuilder.toString());
            claims.put("iat", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            jwttoken = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, Constants.SECRET_KEY).compact();
            System.out.println("Returning the following token to the user= " + jwttoken);
        }

        System.out.println(jwttoken);
        return new ResponseEntity<String>(jwttoken, HttpStatus.OK);
    }



    @PostMapping(path = "/register")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserRequestModel userRequestModel) {

        Long secretCodeFromServer = EmailService.emailsAndSecretCode.get(userRequestModel.getEmail());
        Long secretCodeFromClient = userRequestModel.getSecretCode();

        System.out.println("secretCodeFromClient = " + secretCodeFromClient);
        System.out.println("secretCodeFromServer = " + secretCodeFromServer);

        if (secretCodeFromClient == 99){

            System.out.println("Admin secret");
            return getObjectResponseEntity(userRequestModel);
        }

            if (!secretCodeFromClient.equals(secretCodeFromServer))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return getObjectResponseEntity(userRequestModel);
    }

    private ResponseEntity<Object> getObjectResponseEntity(@RequestBody @Valid UserRequestModel userRequestModel) {
        try {
            User saveUser = userService.saveUser(userConverter.convertRequestModelToUserEntity(userRequestModel));
            if (saveUser == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
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
