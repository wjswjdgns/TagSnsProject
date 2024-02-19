package portfolio.CronProject.web;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import portfolio.CronProject.domain.Member;
import portfolio.CronProject.web.argumentresolver.Login;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ExceptionController {
    // Url로 접근 할려고 했을 경우 메인으로 리다이렉트 시킨다.

    @GetMapping("/compose/comment")
    public String commentPost(@Login Member loginMember) {
        if(loginMember == null){
            return "redirect:members/login";
        }
        return "redirect:/";
    }

    @GetMapping("/compose/edit")
    public String editPost(@Login Member loginMember) {

        if(loginMember == null){
            return "redirect:members/login";
        }
        return "redirect:/";
    }

    @GetMapping("/compose/detail")
    public String redirectDetail(@Login Member loginMember) {
        if(loginMember == null){
            return "redirect:members/login";
        }
        return "redirect:/";
    }


    @GetMapping("/members/update")
    public String update(@Login Member loginMember) {
        if(loginMember == null){
            return "redirect:members/login";
        }
        return "redirect:/";
    }

    @GetMapping("/members/logout")
    public String logout(@Login Member loginMember) {
        if(loginMember == null){
            return "redirect:members/login";
        }
        return "redirect:/";
    }

    @GetMapping("/members/delete")
    public String delete(@Login Member loginMember){
        if(loginMember == null){
            return "redirect:members/login";
        }
        return "redirect:/";
    }


    @GetMapping("/write/default")
    public String write(@Login Member loginMember) {
        if(loginMember == null){
            return "redirect:members/login";
        }
        return "redirect:/";
    }

    @GetMapping("/write/comment")
    public String comment(@Login Member loginMember){
        if(loginMember == null){
            return "redirect:members/login";
        }
        return "redirect:/";
    }

    @GetMapping("/write/edit")
    public String editPostResponse(@Login Member loginMember){
        if(loginMember == null){
            return "redirect:members/login";
        }
        return "redirect:/";
    }

    @GetMapping("/write/delete")
    public String deletePost(@Login Member loginMember){
        if(loginMember == null){
            return "redirect:members/login";
        }
        return "redirect:/";
    }

    @GetMapping("/retwit/add")
    public String Addretwit(@Login Member loginMember){
        if(loginMember == null){
            return "redirect:members/login";
        }
        return "redirect:/";
    }

    @GetMapping("/retwit/cancel")
    public String Cancelretwit(@Login Member loginMember){
        if(loginMember == null){
            return "redirect:members/login";
        }
        return "redirect:/";
    }


    @GetMapping("/like/add")
    public String likePost(@Login Member loginMember){
        if(loginMember == null){
            return "redirect:members/login";
        }
        return "redirect:/";
    }

    @GetMapping("/like/cancel")
    public String likeCancel(@Login Member loginMember){
        if(loginMember == null){
            return "redirect:members/login";
        }
        return "redirect:/";
    }

}
