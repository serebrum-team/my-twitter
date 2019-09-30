package uz.serebrum.mytwitter.service;

import uz.serebrum.mytwitter.entity.User;

public interface PrincipalService {

    User loadByUserName(String userName);
    User loadByUserId(Long userId);
}
