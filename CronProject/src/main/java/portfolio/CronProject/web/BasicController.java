package portfolio.CronProject.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import portfolio.CronProject.domain.Member;
import portfolio.CronProject.service.FollowService;
import portfolio.CronProject.service.MemberService;
import portfolio.CronProject.service.PostService;
import portfolio.CronProject.web.argumentresolver.Login;
import portfolio.CronProject.web.dto.FilterDto;
import portfolio.CronProject.web.dto.MainDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * */

@Controller
@Slf4j
@RequiredArgsConstructor
public class BasicController {
    private final MemberService memberService;
    private final FollowService followService;
    private final PostService postService;

    // 세션이 없는 경우 리다이렉트로 members/login으로
    // 세션이 있는 경우 메인으로 이동할 수 있도록 한다.

//    @RequestMapping("/")  /*홈 화면으로 지정*/
//    public String home(){
//        return "basic/index";
//    }

    // 로그인 안한 사람도 들어오기 때문에 required = false로
    @GetMapping("/")
    public String home(@Login Member loginMember, Model model){

        // 세션에 회원 데이터가 없으면 home
        if(loginMember == null){
            return "redirect:members/login";
        }

        Boolean allMember = true;
        Long findId = null; // 초기에 찾는 Id가 없도록

        // 세션이 유지되면 로그인으로 이동
        List<Member> followList = followService.getFollowingMemberList(loginMember.getId());
        List<FilterDto> filterList = followList.stream()
                .map(o -> new FilterDto(o, findId))
                .collect(Collectors.toList());

        // if문을 통해서 팔로우가 있는지 없는지에 따라서
        // 포스트의 목록을 다르게 담아야 한다.
        List<MainDto> mainPostList = postService.getMainPostList(loginMember.getId());
        // 시간 새롭게 담기

        model.addAttribute("checkAllMember", allMember);
        model.addAttribute("member", memberService.findOne(loginMember.getId()));
        model.addAttribute("followlist", filterList);
        model.addAttribute("postList", mainPostList);


        // 메인화면에서 필요한 것은 회원이 존재하는가? && 노출 시킬 회원의 닉네임과 아이디
        return "basic/main";
    }

    @PostMapping("/")
    public String selectFollow(@Login Member loginMember, String personal, Model model){
        // 세션에 회원 데이터가 없으면 home
        if(loginMember == null){
            return "redirect:members/login";
        }

        Member member = memberService.findPersonal(personal);

        Boolean allMember = false;

        // 세션이 유지되면 로그인으로 이동
        List<Member> followList = followService.getFollowingMemberList(loginMember.getId());
        List<FilterDto> filterList = followList.stream()
                .map(o -> new FilterDto(o, member.getId()))
                .collect(Collectors.toList());


        // 포스트의 목록을 다르게 담아야 한다.
        List<MainDto> mainSelectPostList = postService.getMainSelectPostList(loginMember.getId(), member.getId());


        model.addAttribute("checkAllMember", allMember);
        model.addAttribute("member", memberService.findOne(loginMember.getId()));
        model.addAttribute("followlist", filterList);
        model.addAttribute("postList", mainSelectPostList);

        // 메인화면에서 필요한 것은 회원이 존재하는가? && 노출 시킬 회원의 닉네임과 아이디
        return "basic/main";
    }


}
