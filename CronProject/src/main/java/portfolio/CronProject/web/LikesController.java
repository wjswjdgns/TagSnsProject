package portfolio.CronProject.web;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import portfolio.CronProject.domain.Member;
import portfolio.CronProject.domain.Post;
import portfolio.CronProject.domain.PostComment;
import portfolio.CronProject.service.*;
import portfolio.CronProject.web.argumentresolver.Login;
import portfolio.CronProject.web.form.LikeForm;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LikesController {
    private final PostService postService;
    private final MemberService memberService;
    private final LikeService likeService;
    private final CommentService commentService;
    private final NotificationsService notificationsService;

    @PostMapping("/like/add")
    public void likePost(@RequestBody LikeForm likeForm, @Login Member loginMember ){
        Member member = memberService.findOne(loginMember.getId());
        if(likeForm.isCheckPost()){
            Post post = postService.getPostOne(likeForm.getCheckId());
            likeService.likePost(member,post);// 알림 등록
            notificationsService.saveReactionNotification(loginMember.getId(), likeForm.getCheckId(), true, 2);
        }
        else{
            PostComment postcomment = commentService.getCommentOne(likeForm.getCheckId());
            likeService.likeComment(member,postcomment);
            notificationsService.saveReactionNotification(loginMember.getId(), likeForm.getCheckId(), false, 2);
        }
    }

    @PostMapping("/like/cancel")
    public void likeCancel(@RequestBody LikeForm likeForm , @Login Member loginMember){
        Member member = memberService.findOne(loginMember.getId());

        if(likeForm.isCheckPost()){
            likeService.cancelPost(member.getId(), likeForm.getCheckId());
        }
        else{
            likeService.cancelComment(member.getId(), likeForm.getCheckId());
        }

    }

}
