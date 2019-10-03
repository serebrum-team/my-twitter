package uz.serebrum.mytwitter.service;

import java.util.List;

public interface SearchingService {

    boolean alreadyHasThisUserName(String userName);

    List<Long> getAllMatchedPosts(String word);
}
