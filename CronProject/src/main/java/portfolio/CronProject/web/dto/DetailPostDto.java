package portfolio.CronProject.web.dto;

import lombok.Data;
import portfolio.CronProject.domain.Post;
import portfolio.CronProject.domain.PostComment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static portfolio.CronProject.service.PostService.*;

@Data
public class DetailPostDto {

    private Long postId;

    // 회원 정보
    private String nickName;
    private String personal;
    private String profileImg;

    // 태그 정보
    private String TagName;
    private String TagMemberPersonal;

    // 글 정보
    private String content;
    private String imgUrl;
    private LocalDateTime updateAt;
    private String fUpdateAt; // 포맷팅 된 시간

    // 댓글 정보
    private List<DetailCommentDto> commentList;

    // 카운트 정보
    private Integer commentCount;
    private Integer retwitCount;
    private Integer likeCount;

    // 체크 정보 (로그인 회원이 좋아요 눌렀는지 안눌렀는지 확인)
    private boolean likeCheck;
    private boolean retwitCheck;
    private boolean postCheck;

    //추가 정보
    private boolean loginMemberPost; // 로그인 한 멤버가 작성한 포스트인지 확인
    public DetailPostDto(Post post, Long loginId) {
        postId = post.getId();
        nickName = post.getMember().getNickName();
        personal = post.getMember().getPersonal();
        profileImg = post.getMember().getProfileImg();
        if(post.getUserTag() != null){
            TagName = post.getUserTag().getTagName();
            TagMemberPersonal = post.getUserTag().getMember().getPersonal();
        }
        content = post.getContent();
        imgUrl = generateImageUrl(post);
        updateAt = post.getUpdateAt();
        commentCount = post.getCommentCount();
        retwitCount = post.getRetwitCount();
        likeCount = post.getLikeCount();
        likeCheck = isCheckByLike(post.getLikes(),loginId);
        retwitCheck = isCheckByRetwit(post.getRetwits(), loginId);
        fUpdateAt = fommatingTime(post.getUpdateAt());
        postCheck = true;
        loginMemberPost = isLoginMember(loginId, post.getMember().getId());


    }

    public DetailPostDto(PostComment postComment, Long loginId) {
        postId = postComment.getId();
        nickName = postComment.getMember().getNickName();
        personal = postComment.getMember().getPersonal();
        profileImg = postComment.getMember().getProfileImg();
        content = postComment.getCommentText();
        imgUrl = generateImageUrl(postComment);
        updateAt = postComment.getUpdateAt();
        commentCount = postComment.getCommentCount();
        retwitCount = postComment.getRetwitCount();
        likeCount = postComment.getLikeCount();
        likeCheck = isCheckByLike(postComment.getLikes(),loginId);
        retwitCheck = isCheckByRetwit(postComment.getRetwits(), loginId);
        fUpdateAt = fommatingTime(postComment.getUpdateAt());
        postCheck = false;
        loginMemberPost = isLoginMember(loginId, postComment.getMember().getId());
    }

    private String fommatingTime(LocalDateTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY년 MM월 dd일");
        return time.format(formatter);
    }

    private boolean isLoginMember(Long loginId, Long CommentMemberId){
        if(loginId == CommentMemberId){
            return true;
        }
        else{
            return false;
        }
    }

}

