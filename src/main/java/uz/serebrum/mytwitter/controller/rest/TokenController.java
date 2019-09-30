package uz.serebrum.mytwitter.controller.rest;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uz.serebrum.mytwitter.configuration.Constants;
import uz.serebrum.mytwitter.entity.User;
import uz.serebrum.mytwitter.request.model.LoginRequestModel;
import uz.serebrum.mytwitter.service.UserService;

import javax.servlet.ServletException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TokenController {

	@Autowired
	private UserService userService;

	@PostMapping("/api/v1/token")
	public ResponseEntity<String> getToken(@RequestBody LoginRequestModel login) throws ServletException {

		String jwttoken = "";

        System.out.println(login);
		if(login.getUsername().isEmpty() || login.getPassword().isEmpty())
			return new ResponseEntity<String>("Username or password cannot be empty.", HttpStatus.BAD_REQUEST);

		String name = login.getUsername(), 
				password = login.getPassword();

		System.out.println(name + "  " + password);
		if(!userService.isUser(name,password))
			return new ResponseEntity<String>("Invalid credentials. Please check the username and password.",
					HttpStatus.UNAUTHORIZED);
		else {

			System.out.println(name);
			User byUserName = userService.getByUserName(name);
			// Creating JWT using the user credentials.

			StringBuilder stringBuilder = new StringBuilder();
			byUserName.getAuthorities().forEach(o -> {
				stringBuilder.append(o+" ");
			});
			Map<String, Object> claims = new HashMap<String, Object>();
			claims.put("usr", login.getUsername());
			claims.put("sub", "Authentication token");
			claims.put("iss", Constants.ISSUER);
			claims.put("rol", stringBuilder.toString());
			claims.put("iat", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

			jwttoken = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, Constants.SECRET_KEY).compact();
			System.out.println("Returning the following token to the user= "+ jwttoken);
		}

        System.out.println(jwttoken);
		return new ResponseEntity<String>(jwttoken, HttpStatus.OK);
	}
}