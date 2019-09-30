package uz.serebrum.mytwitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public static HashMap<String,Long> emailsAndSecretCode = new HashMap<>();

    public void sendSecretCode(String email){
        long secretNumber = getRandomDoubleBetweenRange(100,1000);
        String message = "Hi guy . This code from my-twitter app . Code  "+ String.valueOf(secretNumber);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Secret code");
        simpleMailMessage.setText(message);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom("makhmudovbekhruzish@gmail.com");

        javaMailSender.send(simpleMailMessage);

        Long secretCode =  (long) secretNumber;
        emailsAndSecretCode.put(email,secretCode);
    }


    private long getRandomDoubleBetweenRange(double min, double max){
        long x = (long) ((long) (Math.random()*((max-min)+1))+min);
        return x;
    }

    public void sendSuccessRegisterMail(String email,String username){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Registered success");
        simpleMailMessage.setText("You are registered success . Your username " + username);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom("makhmudovbekhruzish@gmail.com");
        javaMailSender.send(simpleMailMessage);
    }
}
