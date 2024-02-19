package portfolio.CronProject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.CronProject.domain.Follow;
import portfolio.CronProject.domain.Member;
import portfolio.CronProject.repository.FollowRepository;
import portfolio.CronProject.repository.MemberRepository;
import portfolio.CronProject.web.dto.FilterDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;




    // 팔로우 하기
    public Long following(Long loginId, Long followId){

        Member member = memberRepository.findOne(loginId);
        Follow follow = new Follow();
        follow.setMember(member);
        follow.setFollowMemberId(followId);

        followRepository.save(follow);


        return followId;
    }

    //팔로우 취소하기
    public void followCancel(Long loginId, Long followId){
        followRepository.cancel(loginId, followId);
    }

    // 마이페이지 팔로우 상태 확인
    public String mypageStatus(Long loginMemberId, Long mypageMemberId){
        String status;
        if(mypageMemberId.equals(loginMemberId)){
            status = "isOwnProfile";
        }
        else if(isFollowing(loginMemberId, mypageMemberId)){
            status = "isFollowingProfile";
        }
        else{
            status = "isUnknownProfile";
        }
        return status;
    }

    // 팔로우 여부 확인 기능
    public boolean isFollowing(Long memberId, Long mypageId){
        Member member = memberRepository.findOne(memberId);
        List<Follow> followlist = member.getFollow();

        boolean check = false;

        for(Follow follow : followlist){
            if(follow.getFollowMemberId().equals(mypageId)){
                check = true;
                break;
            }
        }
        return check;
    }


    // 팔로잉 목록 조회
    public List<Member> getFollowingMemberList(Long memberId){
        return memberRepository.findFollowingMember(memberId);
    }

    // 팔로워 수 반환
    public Integer getFollowerCount(Long memberId){
        List<Follow> followerByMember = followRepository.findFollowerByMember(memberId);
        if(!followerByMember.isEmpty()){
            return followerByMember.size();
        }
        else{
            return 0;
        }
    }

    // 팔로잉 수 반환
    public Integer getFollowingCount(Long memberId){
        List<Follow> followingByMember = followRepository.findFollowingByMember(memberId);
        if(!followingByMember.isEmpty()){
            return followingByMember.size();
        }
        else{
            return 0;
        }
    }





}
