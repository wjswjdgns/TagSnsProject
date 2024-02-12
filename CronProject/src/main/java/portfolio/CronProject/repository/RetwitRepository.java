package portfolio.CronProject.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import portfolio.CronProject.domain.*;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RetwitRepository {
    private final EntityManager em;

    // 리트윗 등록
    public void saveRetwit(Retwit retwit){
        em.persist(retwit);
    }


    public void removeRetwitPost(Long loginId, Long postId){
        Member member = em.find(Member.class, loginId);
        Post post = em.find(Post.class, postId);

        em.createQuery("delete from Retwit r where r.member = :member and r.post = :post")
                .setParameter("member", member)
                .setParameter("post", post)
                .executeUpdate();
    }

    public void removeRetwitComment(Long loginId, Long commentId){
        Member member = em.find(Member.class, loginId);
        PostComment postComment = em.find(PostComment.class, commentId);

        em.createQuery("delete from Retwit r where r.member = :member and r.postComment = :postComment")
                .setParameter("member", member)
                .setParameter("postComment", postComment)
                .executeUpdate();
    }


    // 특정 리트윗들 가져오기
    public List<Retwit> findRetwitList(Long id){
        Member member = em.find(Member.class, id);
        return em.createQuery("select r from Retwit r " +
                        "left join fetch r.post p " +
                        "left join fetch p.userTag pu " +
                        "left join fetch pu.member pum " +
                        "left join fetch p.member m " +
                        "left join fetch r.postComment pc " +
                        "left join fetch pc.member pcm " +
                        "where r.member = :member " +
                        "order by r.createAt desc", Retwit.class)
                .setParameter("member", member)
                .getResultList();
    }


    //팔로우들의 리트윗 리스트 가져오기
    public List<Retwit> findRetwitListAll(List<Member> followList){
        return em.createQuery(
                        "select r from Retwit r " +
                                "left join fetch r.post p " +
                                "left join fetch p.userTag pu " +
                                "left join fetch pu.member pum " +
                                "left join fetch p.member m " +
                                "left join fetch r.postComment pc " +
                                "left join fetch pc.member pcm " +
                                "where r.member in :followMemberList " +
                                "order by r.createAt desc", Retwit.class)
                .setParameter("followMemberList", followList)
                .getResultList();
    }


    // 좋아요 한 리트윗들 가져오기 (포스트)
    public List<Retwit> findLikeListPost(Long id){
        Member member = em.find(Member.class, id);
        return em.createQuery("select r from Retwit r " +
                "left join fetch r.post p " +
                "left join fetch p.userTag pu " +
                "left join fetch pu.member pum " +
                "left join fetch p.member pm " +
                "left join fetch p.likes pl " +
                "where pl.member = :member " +
                "order by r.createAt desc", Retwit.class)
                .setParameter("member", member)
                .getResultList();
    }



    // 좋아요 한 리트윗들 가져오기 (댓글)
    public List<Retwit> findLikeListComment(Long id){
        Member member = em.find(Member.class, id);
        return em.createQuery("select r from Retwit r " +
                "left join fetch r.postComment pc " +
                "left join fetch pc.member pcm " +
                "left join fetch pc.likes pcl " +
                "where pcl.member = :member " +
                "order by r.createAt desc", Retwit.class)
                .setParameter("member",member)
                .getResultList();
    }



}
