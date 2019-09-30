package uz.serebrum.mytwitter.service;

import uz.serebrum.mytwitter.entity.Comment;
import uz.serebrum.mytwitter.entity.Post;
import uz.serebrum.mytwitter.entity.User;
import uz.serebrum.mytwitter.request.model.CommentRequestModel;
import uz.serebrum.mytwitter.request.model.PostRequestModel;
import uz.serebrum.mytwitter.request.model.UserRequestModel;

public interface UpdateService {
    User updateUserAccount(Long userId,UserRequestModel user);
    Post updatePost(Long userId,Long postId,PostRequestModel post);
    Comment updateComment(Long userId,Long commentId, CommentRequestModel comment);
}
