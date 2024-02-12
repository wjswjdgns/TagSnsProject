package portfolio.CronProject.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import portfolio.CronProject.domain.Member;
import portfolio.CronProject.domain.Post;
import portfolio.CronProject.domain.UserTag;
import portfolio.CronProject.service.LoginService;
import portfolio.CronProject.service.MemberService;
import portfolio.CronProject.service.PostService;
import portfolio.CronProject.service.TagService;
import portfolio.CronProject.web.argumentresolver.Login;
import portfolio.CronProject.web.dto.FollowTagsDto;
import portfolio.CronProject.web.dto.PrevPostDto;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class WriteController {

    private final MemberService memberService;
    private final PostService postService;

    private final TagService tagService;


}
