package portfolio.CronProject.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import portfolio.CronProject.domain.Member;
import portfolio.CronProject.service.LoginService;
import portfolio.CronProject.web.argumentresolver.Login;
import portfolio.CronProject.web.form.LoginForm;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/members/login")
    public String loginForm(LoginForm loginform){
        return "basic/index";
    }

    @PostMapping("/members/login")
    public String login(@Valid LoginForm loginform, BindingResult bindingResult, HttpServletRequest request){
        // 아이디 혹은 비밀번호 규칙이 틀렸을 경우
        if(bindingResult.hasErrors()){
            return "basic/index";
        }

        Member loginMember = loginService.login(loginform.getName(), loginform.getPassword());
        // 반환된 값이 없는 경우 (회원이 없거나 비밀번호가 틀렸을 경우)
        if(loginMember == null){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "basic/index";
        }

        // 로그인 성공   (홈으로 보낸다)
        // 세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession(); //request.getSessing(true) 기본값 (true : 세션이 없으면 새로 생성, false : 세션이 없으면 null 반환)
        // 세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/";
    }

    // 로그아웃
    // 로그아웃 진행 시 세션을 삭제하고 리다이렉트로 홈으로 보낸다
    @PostMapping("/members/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate(); // 세션 날리기
        }
        return "redirect:/";
    }

}
