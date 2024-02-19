package portfolio.CronProject.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import portfolio.CronProject.domain.Member;
import portfolio.CronProject.domain.UserTag;
import portfolio.CronProject.service.FollowService;
import portfolio.CronProject.service.MemberService;
import portfolio.CronProject.service.PostService;
import portfolio.CronProject.service.TagService;
import portfolio.CronProject.web.argumentresolver.Login;
import portfolio.CronProject.web.dto.MainDto;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MypageController {

    private final FollowService followService;
    private final MemberService memberService;
    private final TagService tagService;
    private final PostService postService;


    /**
     *
     * 마이페이지 본인 게시물
     *
     * */
    @GetMapping("/{personal}")
    public String profile(@PathVariable("personal") String personal, @Login Member loginMember, Model model){

        //마이페이지에 들어온 회원 정보를 노출하기
        Member MypageMember = memberService.findPersonal(personal);


        //탭 버튼 상태 전달ㄹ
        model.addAttribute("mypageTab", "default");

        // 회원 목록
        String status = followService.mypageStatus(loginMember.getId(), MypageMember.getId());
        model.addAttribute("mypageStatus", status);

        // 팔로우, 팔로잉 목록 담기
        Integer followingCount = followService.getFollowingCount(MypageMember.getId());
        Integer followerCount = followService.getFollowerCount(MypageMember.getId());

        Map<String, Integer> counts = new ConcurrentHashMap<>();
        counts.put("followingCount", followingCount);
        counts.put("followerCount", followerCount);
        model.addAttribute("counts", counts);

        //포스트 목록 담기
        List<MainDto> postList = postService.getMypagePostList(MypageMember.getId());

        model.addAttribute("postList", postList);
        model.addAttribute("postCount", postList.size());

        // 마이페이지 대상자의 정보를 담아서 넘긴다.
        model.addAttribute("member", MypageMember);
        model.addAttribute("loginMember", loginMember.getPersonal());

        // 포스트 총 횟수를 찾아서 전달을 해야 한다.
        return "basic/mypage";
    }

    /**
     *
     * 마이페이지 댓글 게시물
     *
     * */
    @GetMapping("/{personal}/comments")
    public String profileComment(@PathVariable("personal") String personal, @Login Member loginMember, Model model){

        //마이페이지에 들어온 회원 정보를 노출하기
        Member MypageMember = memberService.findPersonal(personal);

        //탭 버튼 상태 전달
        model.addAttribute("mypageTab", "comments");

        // 회원 목록
        String status = followService.mypageStatus(loginMember.getId(), MypageMember.getId());
        model.addAttribute("mypageStatus", status);

        // 팔로우, 팔로잉 목록 담기
        Integer followingCount = followService.getFollowingCount(MypageMember.getId());
        Integer followerCount = followService.getFollowerCount(MypageMember.getId());

        Map<String, Integer> counts = new ConcurrentHashMap<>();
        counts.put("followingCount", followingCount);
        counts.put("followerCount", followerCount);
        model.addAttribute("counts", counts);

        // 포스트 목록 담기
        List<MainDto> postList = postService.getMypageCommentList(MypageMember.getId());

        model.addAttribute("postList", postList);
        model.addAttribute("postCount", MypageMember.getPost().size());

        // 마이페이지 대상자의 정보를 담아서 넘긴다.
        model.addAttribute("member", MypageMember);
        model.addAttribute("loginMember", loginMember.getPersonal());

        // 포스트 총 횟수를 찾아서 전달을 해야 한다.
        return "basic/mypage";
    }

    /**
     *
     * 마이페이지 좋아요 게시물
     *
     * */

    @GetMapping("/{personal}/likes")
    public String profileLike(@PathVariable("personal") String personal, @Login Member loginMember, Model model){

        //마이페이지에 들어온 회원 정보를 노출하기
        Member MypageMember = memberService.findPersonal(personal);

        //탭 버튼 상태 전달
        model.addAttribute("mypageTab", "likes");

        // 회원 목록
        String status = followService.mypageStatus(loginMember.getId(), MypageMember.getId());
        model.addAttribute("mypageStatus", status);

        // 팔로우, 팔로잉 목록 담기
        Integer followingCount = followService.getFollowingCount(MypageMember.getId());
        Integer followerCount = followService.getFollowerCount(MypageMember.getId());

        Map<String, Integer> counts = new ConcurrentHashMap<>();
        counts.put("followingCount", followingCount);
        counts.put("followerCount", followerCount);
        model.addAttribute("counts", counts);

        // 포스트 목록 담기
        List<MainDto> postList = postService.getMypageLikeList(MypageMember.getId());

        model.addAttribute("postList", postList);
        model.addAttribute("postCount", MypageMember.getPost().size());

        // 마이페이지 대상자의 정보를 담아서 넘긴다.
        model.addAttribute("member", MypageMember);
        model.addAttribute("loginMember", loginMember);

        // 포스트 총 횟수를 찾아서 전달을 해야 한다.
        return "basic/mypage";
    }


    @GetMapping("/{personal}/settings/profile")
    public String updateProfile(@Login Member loginMember, Model model){

        // 왜 이전 정보를 그대로 가지고 있는거지?
        // 세션에서 불러오는게 아니라 db에서 불러와야 하는걸까?
        // updateForm을 활용하여서 넣도록 해보자

        // 멤버 담기
        Member member = memberService.findOne(loginMember.getId());
        model.addAttribute("member", member);

        // 멤버의 태그 담기
        List<UserTag> tagList = tagService.getTagList(loginMember.getId());
        model.addAttribute("UserTags", tagList);

        return "basic/update";
    }



    @PostMapping("/follow/new/{personal}")
    public String create(@Login Member loginMember, @PathVariable("personal") String personal) {

        Member mypageMember = memberService.findPersonal(personal);
        followService.following(loginMember.getId(), mypageMember.getId());

        return "redirect:/{personal}";
    }


    @PostMapping("/follow/cancel/{personal}")
    public String cancel(@Login Member loginMember, @PathVariable("personal") String personal){
        Member mypageMember = memberService.findPersonal(personal);
        followService.followCancel(loginMember.getId(), mypageMember.getId());

        return "redirect:/{personal}";
    }

}
