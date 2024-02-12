package portfolio.CronProject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.CronProject.domain.*;
import portfolio.CronProject.repository.CommentRepository;
import portfolio.CronProject.repository.ImageRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ImageRepository imageRepository;

    /**
     *
     * 댓글 등록하기
     *
     * */
    public Long comment(Member member, Post post, String commentWord, Long postImageId){
        PostComment postComment = new PostComment();
        postComment.setMember(member);
        postComment.setPost(post);
        postComment.setCommentText(commentWord);

        if(postImageId != null){
            PostImage postImage = imageRepository.findPostImage(postImageId);
            postComment.setPostImage(postImage);
        }

        postComment.setCreateAt(LocalDateTime.now()); // 등록 시간
        postComment.setUpdateAt(LocalDateTime.now()); // 수정 시간
        postComment.setLikeCount(0);
        postComment.setCommentCount(0);
        postComment.setRetwitCount(0);

        commentRepository.saveComment(postComment);
        post.addStock("comment");

        return postComment.getId();
    }

    /**
     * 수정하기
     * */
    public void editPost(Long postId, String content,Long postImageId, boolean checkImage){
        PostComment comment = commentRepository.findComment(postId);
        comment.setCommentText(content);

        if(!checkImage && comment.getPostImage() != null){
            comment.setPostImage(null);
        }

        // 수정 시 이미지 삭제를 생각해야 하는구나
        if(postImageId != null){
            PostImage postImage = imageRepository.findPostImage(postImageId);
            comment.setPostImage(postImage);
        }



        comment.setUpdateAt(LocalDateTime.now()); // 수정 시간 체크


    }

    /**
     *
     * 대댓글 등록하기
     *
     * */
    public Long recomment(Member member, PostComment comment, String commentWord, Long postImageId){
        PostComment postComment = new PostComment();
        postComment.setMember(member);
        postComment.setParentComment(comment);
        postComment.setCommentText(commentWord);

        if(postImageId != null){
            PostImage postImage = imageRepository.findPostImage(postImageId);
            postComment.setPostImage(postImage);
        }

        postComment.setCreateAt(LocalDateTime.now()); // 등록 시간
        postComment.setUpdateAt(LocalDateTime.now()); // 수정 시간
        postComment.setLikeCount(0);
        postComment.setCommentCount(0);
        postComment.setRetwitCount(0);
        commentRepository.saveComment(postComment);

        comment.addStock("comment");
        return postComment.getId();
    }


    /**
     *
     * 댓글 찾기
     *
     * */
    public PostComment getCommentOne(Long commentId){
        return commentRepository.findComment(commentId);
    }


    /**
     *
     * 댓글 삭제
     *
     * */
     public void deleteComment(Long commentId){
         commentRepository.deleteComment(commentId);
     }

}
