package portfolio.CronProject.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import portfolio.CronProject.domain.*;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {
    private final EntityManager em;

    // Post 등록
    public void save(Post post) {
        em.persist(post);
    }


    //Post 한개 반환
    public Post findPost(Long postId) {
        return em.find(Post.class, postId);
    }


    // Post 삭제하기
    public void deletePost(Long postId) {
        Post post = em.find(Post.class, postId);
        em.remove(post);
    }

    // 팔로우 리스트에서 목록 반환
    public List<Post> findFollowPostList(Long loginId, List<Member> followList) {
        Member member = em.find(Member.class, loginId);
        return em.createQuery(
                        "select distinct p from Post p " +
                                "join fetch p.member m " +
                                "left join fetch p.postComments c " +
                                "left join fetch c.member cm " +
                                "left join fetch p.userTag pu " +
                                "left join fetch pu.member pum " +
                                "where p.member = :member or p.member in :followMemberList " +
                                "order by p.createAt desc, c.createAt asc", Post.class)
                .setParameter("member", member)
                .setParameter("followMemberList", followList)
                .getResultList();
    }

    // 특정 인물의 글들을 가져온다.
    public List<Post> findFollowPostListFiler(Long findId){
        Member member = em.find(Member.class, findId);
        return em.createQuery(
                        "select distinct p from Post p " +
                                "join fetch p.member m " +
                                "left join fetch p.postComments c " +
                                "left join fetch c.member cm " +
                                "left join fetch p.userTag pu " +
                                "left join fetch pu.member pum " +
                                "where p.member = :member " +
                                "order by p.createAt desc, c.createAt asc", Post.class)
                .setParameter("member", member)
                .getResultList();
    }


    // 회원이 좋아요 누른 포스트 가져오기
    public List<Post> findLikeList(Long loginId){
        Member member = em.find(Member.class, loginId);
        return em.createQuery("select distinct p from Post p " +
                        "join fetch p.member m " +
                        "join fetch p.likes l " +
                        "left join fetch p.userTag pu " +
                        "left join fetch pu.member pum " +
                        "where l.member = :member " +
                        "order by l.createAt desc", Post.class)
                .setParameter("member", member)
                .getResultList();
    }


    /***
     * 검색 결과
     *
     * */

    // 회원이 검색한 문구에 따른 포스트 가져오기
    public List<Post> findSearchPost(String searchTerm){
        return em.createQuery("select p from Post p " +
                "where lower(p.content) like lower(concat('%', :searchTerm, '%')) " +
                "order by p.createAt desc", Post.class)
                .setParameter("searchTerm", searchTerm)
                .getResultList();

    }

    // 회원이 검색한 태그와 같은 이름의 태그 포스트들을 전부 보여준다 (같은 이름의 태그도 전부 보여준다)
    public List<Post> findSearchTagPost(List<UserTag> selectTagList){
        return em.createQuery("select p from Post p " +
                        "where p.userTag in : selectTagList " +
                        "order by p.createAt desc", Post.class)
                .setParameter("selectTagList", selectTagList)
                .getResultList();
    }


    // 특정 태그와 관련된 포스트 가져오기
    public List<Post> findSearchSelectTagPost(UserTag selectTag){
        return em.createQuery("select p from Post p " +
                "where p.userTag = :selectTag " +
                "order by p.createAt desc", Post.class)
                .setParameter("selectTag", selectTag)
                .getResultList();
    }


}
