package portfolio.CronProject.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import portfolio.CronProject.domain.Member;
import portfolio.CronProject.domain.Post;
import portfolio.CronProject.service.CommentService;
import portfolio.CronProject.service.MemberService;
import portfolio.CronProject.service.NotificationsService;
import portfolio.CronProject.service.PostService;
import portfolio.CronProject.web.argumentresolver.Login;
import portfolio.CronProject.web.dto.NotificationDto;
import portfolio.CronProject.web.dto.NotificationSettingDto;
import portfolio.CronProject.web.form.ComposePostForm;
import portfolio.CronProject.web.form.NotificationForm;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class NotificationsController {

    private final MemberService memberService;
    private final NotificationsService notificationsService;
    private final PostService postService;
    private final CommentService commentService;
    @GetMapping("/notifications/default")
    public String noticeDefaultAll(@Login Member loginMember, Model model){

        // 로그인 멤버 정보 가져오기
        model.addAttribute("member", loginMember);
        // 알림 정보 담기
        List<NotificationDto> notificationList = notificationsService.getNotification(loginMember.getId());
        model.addAttribute("notificationList", notificationList);

        // 탭 버튼 상태 전달
        model.addAttribute("notificationsStatus", "default");

        // 상태 목록 담기
        return "basic/notice";
    }
    @GetMapping("/notifications/tag")
    public String noticeDefaultTag(@Login Member loginMember, Model model){

        // 로그인 멤버 정보 가져오기
        model.addAttribute("member", loginMember);

        // Tag 정보를 뿌릴 수 있도록 한다.
        // 알림 정보 담기
        List<NotificationDto> notificationList = notificationsService.getTagNotification(loginMember.getId());
        model.addAttribute("notificationList", notificationList);

        // 탭 버튼 상태 전달
        model.addAttribute("notificationsStatus", "tag");

        // 상태 목록 담기
        return "basic/notice";
    }

    //
    @PostMapping("/compose/detail")
    public String redirectDetail(@ModelAttribute ComposePostForm composePostForm, @Login Member loginMember){

        log.info("지금 컨트롤러에 들어온거지?");

        String personal = null;
        if(composePostForm.isPostCheck()){
            personal = postService.getPostOne(composePostForm.getPostId()).getMember().getPersonal();
            return "redirect:/"+personal+"/post/"+composePostForm.getPostId();
        }
        else{
            personal = commentService.getCommentOne(composePostForm.getPostId()).getMember().getPersonal();
            return "redirect:/"+personal+"/comment/"+composePostForm.getPostId();
        }
    }


    // Notice Setting
    @GetMapping("/{personal}/notifications")
    public String noticeSetting(@PathVariable("personal") String personal, @Login Member loginMember, Model model){

        // 회원 정보 받아오기
        Member MypageMember = memberService.findPersonal(personal);
        model.addAttribute("member", MypageMember);

        //해당 멤버의 현 알림 상황을 받아오기

        // 태그 알림 (게시판 & 리트윗 or 게시판 & 리트윗 & 댓글)
        NotificationSettingDto notification = notificationsService.getNotificationSetting(loginMember.getId(), MypageMember.getId());
        model.addAttribute("notification", notification);


        //해당 멤버의 태그 받아오기

        return "basic/notice-setting";
    }

    @ResponseBody
    @PostMapping("/{personal}/notifications")
    public void noticeSetting(@RequestBody NotificationForm notificationForm, @PathVariable("personal") String personal, @Login Member loginMember) {

        Member findMember = memberService.findPersonal(personal);


        // 사용자 알림
        // 알림 받지 않기로 했을 경우 삭제
        if(notificationForm.getStatus() == 0){
            notificationsService.removeNotification(loginMember.getId(), findMember.getId());
        }
        else{
            notificationsService.saveNotificationMember(loginMember.getId(), findMember.getId(), notificationForm.getStatus());
        }

        // 태그 알림 설정
        notificationsService.saveNotificationTag(loginMember.getId(), notificationForm.getTagList(), notificationForm.getRemoveList());

    }


    }
