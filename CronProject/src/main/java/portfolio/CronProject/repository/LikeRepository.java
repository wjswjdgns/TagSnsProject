package portfolio.CronProject.repository;


import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import portfolio.CronProject.domain.Likes;
import portfolio.CronProject.domain.Member;
import portfolio.CronProject.domain.Post;
import portfolio.CronProject.domain.PostComment;

@Repository
@RequiredArgsConstructor
public class LikeRepository {
    private final EntityManager em;

    // 좋아요 누르기
    public void save(Likes like){
        em.persist(like);
    }

    // 좋아요 취소하기 (포스트)
    public void cancelPost(Long memberId, Long postId){
        Member member = em.find(Member.class, memberId);
        Post post = em.find(Post.class, postId);
        em.createQuery("delete from Likes l where l.member = :member and l.post = :post")
                .setParameter("member", member)
                .setParameter("post",post)
                .executeUpdate();
    }

    // 좋아요 취소하기 (코멘트)
    public void cancelComment(Long memberId, Long commentId){
        Member member = em.find(Member.class, memberId);
        PostComment postComment = em.find(PostComment.class, commentId);
        em.createQuery("delete from Likes l where l.member = :member and l.postComment = :postComment")
                .setParameter("member", member)
                .setParameter("postComment",postComment)
                .executeUpdate();
    }


}
