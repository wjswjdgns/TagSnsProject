package portfolio.CronProject;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import portfolio.CronProject.domain.*;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class initDb {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit1();
    }
    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;
        public void dbInit1(){
            Member member = new Member();
            member.setName("userA");
            member.setPassword("test123");
            member.setNickName("test");
            member.setPersonal("test");
            member.setCreateAt(LocalDateTime.now());
            member.setProfileImg("/img/default_profile.png"); // 기본 이미지 설정
            member.setBackgroundImg("/img/default_background.png");
            em.persist(member);

            Member member2 = new Member();
            member2.setName("userB");
            member2.setPassword("test123");
            member2.setNickName("test2");
            member2.setPersonal("test2");
            member2.setCreateAt(LocalDateTime.now());
            member2.setProfileImg("/img/default_profile.png"); // 기본 이미지 설정
            member2.setBackgroundImg("/img/default_background.png");
            em.persist(member2);

            Member member3 = new Member();
            member3.setName("userC");
            member3.setPassword("test123");
            member3.setNickName("test3");
            member3.setPersonal("test3");
            member3.setCreateAt(LocalDateTime.now());
            member3.setProfileImg("/img/default_profile.png"); // 기본 이미지 설정
            member3.setBackgroundImg("/img/default_background.png");
            em.persist(member3);

            Member member4 = new Member();
            member4.setName("userD");
            member4.setPassword("test123");
            member4.setNickName("test4");
            member4.setPersonal("test4");
            member4.setCreateAt(LocalDateTime.now());
            member4.setProfileImg("/img/default_profile.png"); // 기본 이미지 설정
            member4.setBackgroundImg("/img/default_background.png");
            em.persist(member4);

            Member member5 = new Member();
            member5.setName("userE");
            member5.setPassword("test123");
            member5.setNickName("test5");
            member5.setPersonal("test5");
            member5.setCreateAt(LocalDateTime.now());
            member5.setProfileImg("/img/default_profile.png"); // 기본 이미지 설정
            member5.setBackgroundImg("/img/default_background.png");
            em.persist(member5);

            Member member6 = new Member();
            member6.setName("userF");
            member6.setPassword("test123");
            member6.setNickName("test6");
            member6.setPersonal("test6");
            member6.setCreateAt(LocalDateTime.now());
            member6.setProfileImg("/img/default_profile.png"); // 기본 이미지 설정
            member6.setBackgroundImg("/img/default_background.png");
            em.persist(member6);


            // 포스트 만들기
            Post post = new Post();
            post.setMember(member); // 등록하는 사람
            post.setContent("테스트 중입니다."); // 등록 할 글
            post.setCreateAt(LocalDateTime.now()); // 등록 시간
            post.setUpdateAt(LocalDateTime.now()); // 수정 시간
            post.setLikeCount(0);
            post.setCommentCount(0);
            post.setRetwitCount(0);
            em.persist(post);


            // 팔로우 등록하기
            Follow follow = new Follow();
            follow.setMember(member);
            follow.setFollowMemberId(member2.getId());
            em.persist(follow);

            //heart 등록하기
            Likes like = new Likes();
            like.setMember(member2);
            like.setPost(post);
            like.setCreateAt(LocalDateTime.now());
            em.persist(like);
            post.addStock("like");

            Likes like2 = new Likes();
            like2.setMember(member3);
            like2.setPost(post);
            like2.setCreateAt(LocalDateTime.now());
            em.persist(like2);
            post.addStock("like");

            Likes like3 = new Likes();
            like3.setMember(member4);
            like3.setPost(post);
            like3.setCreateAt(LocalDateTime.now());
            em.persist(like3);
            post.addStock("like");

            Likes like4 = new Likes();
            like4.setMember(member5);
            like4.setPost(post);
            like4.setCreateAt(LocalDateTime.now());
            em.persist(like4);
            post.addStock("like");

            Likes like5 = new Likes();
            like5.setMember(member6);
            like5.setPost(post);
            like5.setCreateAt(LocalDateTime.now());
            em.persist(like5);
            post.addStock("like");

            //댓글 만들기
            PostComment postComment = new PostComment();
            postComment.setMember(member2);
            postComment.setPost(post);
            postComment.setCommentText("테스트 중입니다.");
            postComment.setCreateAt(LocalDateTime.now());
            postComment.setUpdateAt(LocalDateTime.now());
            postComment.setLikeCount(0);
            postComment.setCommentCount(0);
            postComment.setRetwitCount(0);
            em.persist(postComment);

            post.addStock("comment");


            PostComment postComment2 = new PostComment();
            postComment2.setMember(member3);
            postComment2.setPost(post);
            postComment2.setCommentText("테스트 중입니다.2");
            postComment2.setCreateAt(LocalDateTime.now());
            postComment2.setUpdateAt(LocalDateTime.now());
            postComment2.setLikeCount(0);
            postComment2.setCommentCount(0);
            postComment2.setRetwitCount(0);
            em.persist(postComment2);

            post.addStock("comment");

        }
    }

}

