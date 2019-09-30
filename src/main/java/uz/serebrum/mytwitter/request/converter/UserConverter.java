package uz.serebrum.mytwitter.request.converter;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uz.serebrum.mytwitter.entity.User;
import uz.serebrum.mytwitter.entity.embeddable.OtherUserDetails;
import uz.serebrum.mytwitter.request.model.UserRequestModel;

@Component
public class UserConverter {

    public User convertRequestModelToUserEntity(UserRequestModel userRequestModel){
        User user = new User();
        OtherUserDetails otherUserDetails = new OtherUserDetails();
        otherUserDetails.setBio(userRequestModel.getBio());
        otherUserDetails.setEmail(userRequestModel.getEmail());
        otherUserDetails.setFirstName(userRequestModel.getFirstName());
        otherUserDetails.setLastName(userRequestModel.getLastName());
        user.setUserName(userRequestModel.getUserName());
        user.setPassword(userRequestModel.getPassword());
        user.setOtherUserDetails(otherUserDetails);
        return user;
    }
}
