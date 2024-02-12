package portfolio.CronProject.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import portfolio.CronProject.domain.Member;
import portfolio.CronProject.domain.Post;
import portfolio.CronProject.domain.PostComment;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    // Member 등록
    public void save(Member member){
        em.persist(member);
    }

    // Member 삭제
    public void delete(Long id){
        Member member = em.find(Member.class, id);
        em.remove(member);
    }



    // ID 조회
    // JPA를 활용하여 ID값으로 멤버를 찾는다.
    public Member findOne(Long id){
        return em.find(Member.class, id);
    }


    // 고유 아이디로 회원 찾기
    public Member findPersonal(String personal){
        try{
            return em.createQuery("select m from Member m where m.personal = :personal", Member.class)
                    .setParameter("personal", personal)
                    .getSingleResult();
        }catch(NoResultException e){ // 회원을 찾을 수 없는 경우 null 값 반환
            return null;
        }
    }

    // 로그인 조회
    public Optional<Member> findByLoginId(String name){
        List<Member> all = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();

        for (Member m : all){
            if(m.getName().equals(name)){
                return Optional.of(m);
            }
        }
        return Optional.empty();
    }

    // 중복 조회
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
    public List<Member> findByPersonal(String personal){
        return em.createQuery("select m from Member m where m.personal = :personal", Member.class)
                .setParameter("personal", personal)
                .getResultList();
    }

    // 나를 팔로우한 사람들의 리스트
    public List<Member> findFollowMember(Long loginId){
        Member member = em.find(Member.class, loginId);
        return em.createQuery("select m from Member m " +
                "join fetch m.follow f " +
                "where f.member = :member", Member.class)
                .setParameter("member", member)
                .getResultList();
    }


    // 내가 팔로우 한 사람들 List<Member>로 가져오기
    public List<Member> findFollowingMember(Long loginId){
        Member member = em.find(Member.class, loginId);
        return em.createQuery("select m from Member m " +
                "where m.id IN (select f.followMemberId from Follow f where f.member =: member)", Member.class)
                .setParameter("member", member)
                .getResultList();
    }

}
