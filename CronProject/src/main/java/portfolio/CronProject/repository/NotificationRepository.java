package portfolio.CronProject.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import portfolio.CronProject.domain.*;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationRepository {

    private final EntityManager em;

    //알림 설정하기 (사용자)
    public void saveMember(NoticeMember noticeMember){
        em.persist(noticeMember);
    }

    //알림 설정하기 (태그)
    public void saveTag(NoticeTag noticeTag){
        em.persist(noticeTag);
    }

    // 알림
    public Notice saveNotification(Notice notice){
        em.persist(notice);
        return notice;
    }

    // 타겟의 알림 가져오기
    public List<Notice> findNotification(Long loginId){
        Member member = em.find(Member.class, loginId);
        return em.createQuery("select n from Notice n " +
                        "left join fetch n.post np " +
                        "left join fetch n.postComment npc " +
                        "where n.member = :member " +
                        "order by n.createAt desc", Notice.class)
                .setParameter("member", member)
                .getResultList();
    }

    // 알림 설정 취소하기 (사용자)
    public void cancel(Long loginId, Long findId){
        Member member = em.find(Member.class, loginId);
        em.createQuery("delete from NoticeMember nm where nm.member = :member and nm.activeMemberId = :findId")
                .setParameter("member", member)
                .setParameter("findId",findId)
                .executeUpdate();
    }

    // 알림 설정 취소하기 (태그)
    public void cancelTag(Long loginId, Long tagId){
        Member member = em.find(Member.class, loginId);
        UserTag userTag = em.find(UserTag.class, tagId);
        em.createQuery("delete from NoticeTag nt where nt.member = :member and nt.userTag = :userTag")
                .setParameter("member", member)
                .setParameter("userTag",userTag)
                .executeUpdate();
    }


    // 한 멤버가 설정한 알림 조회하기
    public NoticeMember findNoticeMember(Long loginId, Long findId){
        Member member = em.find(Member.class, findId);
        List<NoticeMember> resultList = em.createQuery("select nm from NoticeMember nm " +
                        "where nm.member = :member and nm.activeMemberId = :loginId", NoticeMember.class)
                .setParameter("member", member)
                .setParameter("loginId", loginId)
                .getResultList();

        if (resultList.isEmpty()){
            return null;
        }
        else{
            return resultList.get(0);
        }

    }

    // 로그인 멤버가 설정한 태그 알림 조회하기
    public NoticeTag findNotificationTags(Long loginId, Long tagId){
        Member member = em.find(Member.class, loginId);
        UserTag userTag = em.find(UserTag.class, tagId);

        List<NoticeTag> resultList = em.createQuery("select nt from NoticeTag nt " +
                        "where nt.member = : member and nt.userTag = : userTag", NoticeTag.class)
                .setParameter("member", member)
                .setParameter("userTag",userTag)
                .getResultList();

        if (resultList.isEmpty()){
            return null;
        }
        else{
            return resultList.get(0);
        }
    }



}
