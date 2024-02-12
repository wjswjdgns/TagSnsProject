package portfolio.CronProject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.CronProject.domain.Member;
import portfolio.CronProject.repository.FollowRepository;
import portfolio.CronProject.repository.MemberRepository;
import portfolio.CronProject.repository.TagRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;


    /***
     * 회원 가입
     *
     * */
    public Long join(Member member){
        validateDuplicateMember(member); // 아이디 체크
        validateDuplicatePersonal(member); // 고유 ID 체크

        // 패스워드 유효성 검사
        if(!validatePassword(member.getPassword())){
            throw new IllegalArgumentException("비밀번호는 영문자와 숫자를 포함해야 합니다.");
        }

        memberRepository.save(member);
        return member.getId();
    }
    /***
     * 회원 탈퇴
     *
     * */
    public void delete(Long loginId){
        followRepository.removeFollow(loginId);
        memberRepository.delete(loginId);
    }


    // 비밀번호 검증 메서드 추가 (문자와 숫자가 반드시 같이 구성되어야 한다)
    private boolean validatePassword(String password) {
        boolean containsLetter = false;
        boolean containsDigit = false;

        for(char c : password.toCharArray()){
            if(Character.isLetter(c)){
                containsLetter = true;
            } else if(Character.isDigit(c)){
                containsDigit = true;
            }

            // 둘 다 찾았으면 루프 종료
            if(containsDigit && containsLetter){
                break;
            }
        }
        return containsLetter && containsDigit;
    }

    // 비즈니스 로직
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());

        if(!findMembers.isEmpty()){
            throw new DuplicateMemberException("중복된 아이디 입니다");
        }
    }

    private void validateDuplicatePersonal(Member member) {
        // 고유 ID
        List<Member> findPersonal = memberRepository.findByPersonal(member.getPersonal());

        if(!findPersonal.isEmpty()){
            throw new DuplicatePersonalException("중복된 고유 ID 입니다.");
        }
    }


    /**
     * 사용자 예외 처리
     * */
    public class DuplicatePersonalException extends IllegalStateException{
        public DuplicatePersonalException(String message) {
            super(message);
        }
    }

    public class DuplicateMemberException extends IllegalStateException{
        public DuplicateMemberException(String message) {
            super(message);
        }
    }


    /***
     *
     *  회원 정보 변경 (프로필 변경)
     */
    public void update(Long id, String nickname, String introduce, String backgroundImgName, String profileImgName){
        Member member = memberRepository.findOne(id);
        member.setNickName(nickname);
        member.setIntroduce(introduce);

        if(backgroundImgName != null){
            member.setBackgroundImg("/uploads/BackgroundImg/"+backgroundImgName);
        }
        if(profileImgName != null){
            member.setProfileImg("/uploads/profileImg/"+profileImgName);
        }
    }


    /**
     *
     * 회원 찾기
     *
     * */
    public Member findOne(Long loginId){
        return memberRepository.findOne(loginId);
    }

    public Member findPersonal(String personal){
        return memberRepository.findPersonal(personal);
    }

}
