package portfolio.CronProject.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import portfolio.CronProject.domain.Member;
import portfolio.CronProject.service.MemberService;
import portfolio.CronProject.service.PostService;
import portfolio.CronProject.service.TagService;
import portfolio.CronProject.web.argumentresolver.Login;
import portfolio.CronProject.web.dto.MainDto;
import portfolio.CronProject.web.dto.SearchTagDto;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SearchController {

    private final MemberService memberService;
    private final PostService postService;
    private final TagService tagService;

    @GetMapping("search")
    public String search(@Login Member loginMember, Model model) {

        Member member = memberService.findOne(loginMember.getId());
        model.addAttribute("member", member);

        List<SearchTagDto> loginRecommended = tagService.getLoginRecommended(loginMember.getId());
        model.addAttribute("followTagList", loginRecommended);

        List<SearchTagDto> recommended = tagService.getRecommended();
        model.addAttribute("recommendedTagList", recommended);


        return "basic/search";
    }


    @GetMapping("search/result")
    public String result(@RequestParam(name="searchField", required = false) String searchField,
                         @Login Member loginMember, Model model){

        List<MainDto> searchPost = postService.getSearchPost(loginMember.getId(), searchField);
        model.addAttribute("postList", searchPost);

        model.addAttribute("searchWord", searchField);

        return "basic/searchresult";
    }


}
