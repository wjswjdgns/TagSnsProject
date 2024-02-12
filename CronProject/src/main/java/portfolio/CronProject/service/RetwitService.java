package portfolio.CronProject.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.CronProject.domain.Member;
import portfolio.CronProject.domain.Post;
import portfolio.CronProject.domain.PostComment;
import portfolio.CronProject.domain.Retwit;
import portfolio.CronProject.repository.CommentRepository;
import portfolio.CronProject.repository.MemberRepository;
import portfolio.CronProject.repository.PostRepository;
import portfolio.CronProject.repository.RetwitRepository;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class RetwitService {

    private final PostRepository postRepository;
    private final RetwitRepository retwitRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;


    /**
     *
     * 리트윗 등록하기
     *
     * */

    public Long retwit(Long loginId, Long postId, boolean check) {
        Member member = memberRepository.findOne(loginId);

        Retwit retwit = new Retwit();
        retwit.setMember(member);
        retwit.setCreateAt(LocalDateTime.now());

        if(check){
            Post post = postRepository.findPost(postId);
            retwit.setPost(post);
            post.addStock("retwit");
        }else{
            PostComment postComment = commentRepository.findComment(postId);
            retwit.setPostComment(postComment);
            postComment.addStock("retwit");
        }

        retwitRepository.saveRetwit(retwit);
        return member.getId();
    }


    /**
     *
     * 리트윗 삭제하기
     *
     * */
    public Long CancelRetwit(Long loginId, Long postId, boolean check){

        if(check){
            Post post = postRepository.findPost(postId);
            post.removeStock("retwit");
            retwitRepository.removeRetwitPost(loginId, postId);
        }
        else{
            PostComment comment = commentRepository.findComment(postId);
            comment.removeStock("retwit");
            retwitRepository.removeRetwitComment(loginId, postId);
        }

        return loginId;
    }

}
