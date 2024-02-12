package portfolio.CronProject.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import portfolio.CronProject.domain.Follow;
import portfolio.CronProject.domain.Member;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FollowRepository {
    private final EntityManager em;


    // 팔로우 진행하기
    public void save(Follow follow){
        em.persist(follow);
    }

    // 팔로우 해제하기
    public void cancel(Long loginId, Long followId){
        Member member = em.find(Member.class, loginId);
        em.createQuery("delete from Follow f where f.member = :member and f.followMemberId = :followId")
                .setParameter("member", member)
                .setParameter("followId",followId)
                .executeUpdate();
    }

    //팔로잉 목록 조회
    public List<Follow> findFollowingByMember(Long id){
        Member member = em.find(Member.class, id);
        if(member != null){
            List<Follow> followList = member.getFollow();
            return followList; // 해당 리스트를 반환한다.
        }
        else{
            return Collections.emptyList(); // 로그인한 사용자가 존재하지 않으면 빈 목록 반환
        }

    }



    // 팔로워 목록 조회
    public List<Follow> findFollowerByMember(Long id){
        return em.createQuery("select f from Follow f where f.followMemberId = :memberId", Follow.class)
                .setParameter("memberId", id)
                .getResultList();
    }


    // 회원 탈퇴 시 팔로우 삭제
    public void removeFollow(Long loginId) {
       em.createQuery("delete from Follow f where f.followMemberId = :MemberId")
                .setParameter("MemberId", loginId)
                .executeUpdate();
    }


}
