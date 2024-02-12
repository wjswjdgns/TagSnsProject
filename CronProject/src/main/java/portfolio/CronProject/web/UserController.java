package portfolio.CronProject.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import portfolio.CronProject.domain.Member;
import portfolio.CronProject.service.ImageService;
import portfolio.CronProject.service.MemberService;
import portfolio.CronProject.service.TagService;
import portfolio.CronProject.web.argumentresolver.Login;
import portfolio.CronProject.web.form.UpdateForm;
import portfolio.CronProject.web.form.UserForm;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;


/**
 * 로그인 및 로그아웃 등 사용자에 관한 컨트롤러가 들어가게 된다.
 * */
@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final MemberService memberService;
    private final TagService tagService;
    private final ImageService imageService;

    @GetMapping("/members/new")
    public String createForm(Model model){
        model.addAttribute("userForm", new UserForm());
        return "basic/signup";
    }


    /**
     * 회원 가입
     * **/

    @PostMapping("/members/new")
    public String create(@Valid UserForm userForm, BindingResult result){

        Member member = new Member();
        member.setName(userForm.getName());
        member.setPassword(userForm.getPassword());
        member.setNickName(userForm.getNickname());
        member.setPersonal(userForm.getPersonal());
        member.setCreateAt(LocalDateTime.now());
        member.setProfileImg("/img/default_profile.png"); // 기본 이미지 설정
        member.setBackgroundImg("/img/default_background.png");


        if(result.hasErrors()){
            return "basic/signup";
        }

        try{
            memberService.join(member);
        }catch (MemberService.DuplicateMemberException ex){
            result.rejectValue("name","error.duplicatedId", ex.getMessage());
            return "basic/signup";
        }catch (MemberService.DuplicatePersonalException ex){
            result.rejectValue("personal","error.duplicatedPersonal", ex.getMessage());
            return "basic/signup";
        }catch (IllegalArgumentException ex){
            result.rejectValue("password", "error.ValidatePassword", ex.getMessage());
            return "basic/signup";
        }

        return "redirect:/members/login";  //로그인 페이지로 넘어간다.
    }
    /**
     * 회원 탈퇴
     * **/
    @ResponseBody
    @PostMapping("/members/delete")
    public void delete(@Login Member loginMember) {
        memberService.delete(loginMember.getId());
    }


    /**
     * 회원 정보 수정
     * **/
    @ResponseBody
    @PostMapping("/members/update")
    public void update(@ModelAttribute UpdateForm updateForm,
                                 @RequestParam(name= "backgroundImg", required = false) MultipartFile backgroundImg,
                                 @RequestParam(name= "profileImg", required = false) MultipartFile profileImg,
                                 @Login Member loginMember) throws IOException {

        String profileName = null;
        String backgroundName = null;

        if(updateForm.getCreateTaglist() == null){
            updateForm.setCreateTaglist(new ArrayList<>());
        }

        // 프로필 이미지 업로드
        if(profileImg != null){
            String profileUrl = loginMember.getProfileImg();
            Path profilePath = Paths.get(profileUrl);
            String OldfileName = profilePath.getFileName().toString();
            // 변경 되었다면 이전 주소 값을 가져와서 삭제한다.
            profileName = imageService.saveProfileImage(profileImg, updateForm.getNickName(), OldfileName);
        }
        if(backgroundImg != null){
            String BackgroundUrl = loginMember.getBackgroundImg();
            Path BackgroundPath = Paths.get(BackgroundUrl);
            String OldfileName = BackgroundPath.getFileName().toString();
            backgroundName = imageService.saveBackgroundImage(backgroundImg, updateForm.getNickName(), OldfileName);
        }


        // 태그의 중복값이 있으면 돌려보내야 한다.
        // 멤버 서비스에서 회원 정보가 수정 되도록
        memberService.update(loginMember.getId(),updateForm.getNickName(),updateForm.getIntroduce(),backgroundName,profileName);
        Member member = memberService.findOne(loginMember.getId());


        // loginMember의 태그가 처음 생성하는 것이 아니라면
        if(!member.getUserTag().isEmpty()){
            // 먼저 생성을 진행한다
            tagService.createExistTag(loginMember.getId(), updateForm.getCreateTaglist());
//            // 이후 없는 것들을 삭제한다.
            tagService.removeTag(loginMember.getId(), updateForm.getCreateTaglist());

            // 태그 삭제 (처음에 삭제하고 전부 생성하는 것을 생각을 했지만 포스트가 등록이 된다면 관련 된 것이 있기 때문에 삭제가 힘들다. 수정 필요)
            // 태그 생성
        }
        else{
            tagService.createTag(loginMember.getId(), updateForm.getCreateTaglist());
        }
    }


}
