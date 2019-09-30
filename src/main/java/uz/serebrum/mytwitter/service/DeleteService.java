package uz.serebrum.mytwitter.service;

public interface DeleteService {

    boolean deleteUserAccount(Long userId);
    boolean deletePost(Long userId,Long postId);
    boolean deleteComment(Long userId,Long commentId);
    boolean deleteFollowed(Long userId,Long memberId);
    boolean deleteMember(Long userId,Long memberId);
}
