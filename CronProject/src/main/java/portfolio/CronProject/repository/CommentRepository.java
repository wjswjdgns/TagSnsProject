package portfolio.CronProject.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import portfolio.CronProject.domain.Member;
import portfolio.CronProject.domain.Post;
import portfolio.CronProject.domain.PostComment;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final EntityManager em;

    // 댓글 등록하기
    public void saveComment(PostComment postComment) {
        em.persist(postComment);
    }

    // 댓글 하나 반환
    public PostComment findComment(Long commentId) {
        return em.find(PostComment.class, commentId);
    }

    // 댓글 삭제 하기
    public void deleteComment(Long commentId) {
        PostComment postComment = em.find(PostComment.class, commentId);
        em.remove(postComment);
    }


    // 특정 글의 댓글 정보 가져오기
    public List<PostComment> findPostComment(Post post){
        return em.createQuery("select pc from PostComment pc " +
                "join fetch pc.member m " +
                "left join fetch pc.childComment pcc " +
                "where pc.post = :post", PostComment.class)
                .setParameter("post", post)
                .getResultList();
    }

    // 특정 댓글의 댓글 정보 가져오기
    public List<PostComment> findCommentComment(PostComment postComment){
        return em.createQuery("select pc from PostComment pc " +
                "join fetch pc.member m " +
                "left join fetch pc.childComment pcc " +
                "where pc.parentComment =: postComment", PostComment.class)
                .setParameter("postComment", postComment)
                .getResultList();
    }

    // 로그인 한 회원의 팔로우들의 댓글들을 노출


    // 나와 팔로우들의 댓글 정보를 반환
    public List<PostComment> findCommentListAll(Long loginId, List<Member> followList){
        Member member = em.find(Member.class, loginId);

        return em.createQuery(
                "select distinct pc from PostComment pc " +
                        "join fetch pc.member m " +
                        "left join fetch pc.post p " +
                        "left join fetch p.userTag pu " +
                        "left join fetch pu.member pum " +
                        "left join fetch pc.parentComment par " +
                        "left join fetch p.member pm " +
                        "left join fetch par.member parm " +
                        "where pc.member = :member or pc.member in :followList " +
                        "order by pc.createAt desc", PostComment.class)
                .setParameter("member", member)
                .setParameter("followList", followList)
                .getResultList();
    }

    // 특정 회원의 댓글 정보를 반환 (부모 값 반환)
    public List<PostComment> ParfindCommentList(Long id){
        Member member = em.find(Member.class, id);
        return em.createQuery(
                        "select distinct pc from PostComment pc " +
                                "join fetch pc.member m " +
                                "left join fetch pc.post p " +
                                "left join fetch p.userTag pu " +
                                "left join fetch pu.member pum " +
                                "left join fetch pc.parentComment par " +
                                "left join fetch p.member pm " +
                                "left join fetch par.member parm " +
                                "where pc.member = :member " +
                                "order by pc.createAt desc", PostComment.class)
                .setParameter("member", member)
                .getResultList();
    }


    // 특정 회원의 좋아요한 댓글 정보를 반환
    public List<PostComment> findLikeList(Long id){
        Member member = em.find(Member.class, id);
        return em.createQuery("select distinct pc from PostComment pc " +
                "join fetch pc.member pcm " +
                "join fetch pc.likes pcl " +
                "where pcl.member = :member " +
                "order by pc.createAt desc", PostComment.class)
                .setParameter("member", member)
                .getResultList();
    }




}
