package portfolio.CronProject.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import portfolio.CronProject.domain.Member;
import portfolio.CronProject.domain.NoticeTag;
import portfolio.CronProject.domain.UserTag;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TagRepository {

    private final EntityManager em;

    // 태그 한개 찾기
    public UserTag findTag(Long tagId){
        return em.find(UserTag.class, tagId);
    }

    // 태그 이름으로 찾기
    public List<UserTag> findNameTag(String tagName){
        return em.createQuery("select t from UserTag t where t.tagName = :tagName", UserTag.class)
                .setParameter("tagName", tagName)
                .getResultList();
    }

    // 태그 삭제하기
    public void deleteTag(List<UserTag> userTagList) {
        for (UserTag userTag : userTagList){
            em.remove(userTag);
        }
    }
    // 태그 생성하기
    public void createTag(UserTag userTag){
        em.persist(userTag);
    }


    // 태그 불러오기
    public List<UserTag> findUserTag(Long id){
        Member member = em.find(Member.class, id);
        if(member != null){
            List<UserTag> memberTags = member.getUserTag();
            return memberTags;
        }
        else{
            return Collections.emptyList();
        }
    }


    // 특정 유저의 특정 태그 불러오기
    public UserTag findSelectUserTag(Member selectMember, String tagName){
        List<UserTag> resultList = em.createQuery("select t from UserTag t " +
                        "where t.member = :selectMember and t.tagName = :tagName", UserTag.class)
                .setParameter("selectMember", selectMember)
                .setParameter("tagName", tagName)
                .getResultList();

        if (resultList.isEmpty()){
            return null;
        }
        else{
            return resultList.get(0);
        }
    }



    // 팔로우들의 태그 불러오기
    // 팔로우 태그 불러오기 진행 중
    public List<UserTag> findFollowUserTag(Member followMember){
        return em.createQuery("select t from UserTag t " +
                "where t.member = :followMember")
                .setParameter("followMember", followMember)
                .getResultList();
    }



    // 태그 리스트 중 로그인 멤버가 활성화 태그 찾기
    public List<UserTag> findSelectTagList(Long loginId, Long findId){
        Member findMember = em.find(Member.class, findId);
        Member member = em.find(Member.class, loginId);
        List<UserTag> resultList = em.createQuery("select t from UserTag t " +
                        "join fetch t.noticeTag tnt " +
                        "where t.member = : findMember and tnt.member = : member", UserTag.class)
                .setParameter("member", member)
                .setParameter("findMember", findMember)
                .getResultList();

        if (resultList.isEmpty()){
            return null;
        }
        else{
            return resultList;
        }
    }

    // 로그인 회원을 위한 태그 추천
    public List<UserTag> findLoginRecommended(List<Member> followMemberList) {
        // 팔로우 기준
        // count가 가장 많은 것
        return em.createQuery("select distinct t from UserTag t " +
                        "join fetch t.member m " +
                        "where t.member in : followMemberList " +
                        "order by t.tagCount desc", UserTag.class)
                .setParameter("followMemberList", followMemberList)
                .setMaxResults(3)
                .getResultList();

    }

    // 인기 급 상승 태그 추천
    public List<UserTag> findRecommended() {
        // 팔로우 기준
        // count가 가장 많은 것
        return em.createQuery("select distinct t from UserTag t " +
                        "join fetch t.member m " +
                        "order by t.tagCount desc", UserTag.class)
                .setMaxResults(5)
                .getResultList();

    }
}
