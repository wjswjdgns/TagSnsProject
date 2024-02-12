package portfolio.CronProject.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import portfolio.CronProject.domain.Member;
import portfolio.CronProject.service.NotificationsService;
import portfolio.CronProject.service.RetwitService;
import portfolio.CronProject.web.argumentresolver.Login;
import portfolio.CronProject.web.form.ComposePostForm;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RetwitController {
    private final RetwitService retwitService;
    private final NotificationsService notificationsService;

    @ResponseBody
    @PostMapping("/retwit/add")
    public void Addretwit(@RequestBody ComposePostForm composePostForm, @Login Member loginMember){
        retwitService.retwit(loginMember.getId(), composePostForm.getPostId(), composePostForm.isPostCheck());
        notificationsService.saveReactionNotification(loginMember.getId(), composePostForm.getPostId(), composePostForm.isPostCheck(), 3);
    }

    @ResponseBody
    @PostMapping("/retwit/cancel")
    public void Cancelretwit(@RequestBody ComposePostForm composePostForm, @Login Member loginMember){
        retwitService.CancelRetwit(loginMember.getId(), composePostForm.getPostId(), composePostForm.isPostCheck());
    }

}
