package portfolio.CronProject.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.CronProject.domain.Likes;
import portfolio.CronProject.domain.Member;
import portfolio.CronProject.domain.Post;
import portfolio.CronProject.domain.PostComment;
import portfolio.CronProject.repository.LikeRepository;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostService postService;
    private final CommentService commentService;

    // 좋아요 등록 (포스트)
    public void likePost(Member member, Post post){
        Likes like = new Likes();
        like.setMember(member);
        like.setPost(post);
        like.setCreateAt(LocalDateTime.now());
        likeRepository.save(like);
        post.addStock("like");


    }

    // 좋아요 등록 (댓글)
    public void likeComment(Member member, PostComment postComment){
        Likes like = new Likes();
        like.setMember(member);
        like.setPostComment(postComment);
        like.setCreateAt(LocalDateTime.now());
        likeRepository.save(like);
        postComment.addStock("like");
    }

    // 좋아요 취소 (포스트)
    public void cancelPost(Long memberId, Long postId){
        likeRepository.cancelPost(memberId, postId);
        Post post = postService.getPostOne(postId);
        post.removeStock("like");
    }

    // 좋아요 취소 (댓글)
    public void cancelComment(Long memberId, Long commentId){
        likeRepository.cancelComment(memberId, commentId);
        PostComment comment = commentService.getCommentOne(commentId);
        comment.removeStock("like");
    }

}
