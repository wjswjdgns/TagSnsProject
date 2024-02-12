package portfolio.CronProject.web.dto;

import lombok.Data;
import portfolio.CronProject.domain.Member;
import portfolio.CronProject.domain.PostComment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static portfolio.CronProject.service.PostService.*;

@Data
public class DetailCommentDto {

    // 회원 정보
    private String nickName;
    private String personal;
    private String profileImg;

    // 글 정보
    private Long postId;
    private boolean postCheck;
    private String content;
    private String imgUrl;
    private LocalDateTime updateAt;
    private String fUpdateAt; // 포맷팅 된 시간

    // 카운트 정보
    private Integer commentCount;
    private Integer retwitCount;
    private Integer likeCount;

    // 체크 정보 (로그인 회원이 좋아요 눌렀는지 안눌렀는지 확인)
    private boolean likeCheck;
    private boolean retwitCheck;

    // 글 작성자가 댓글을 단 경우 해당 댓글을 노출 시키기 위한 요소
    private DetailCommentDto ownerComment;
    private boolean loginMemberComment;

    public DetailCommentDto(PostComment postComment, Long loginId, Member postMember) {
        nickName = postComment.getMember().getNickName();
        personal = postComment.getMember().getPersonal();
        profileImg = postComment.getMember().getProfileImg();
        content = postComment.getCommentText();
        imgUrl = generateImageUrl(postComment);
        updateAt = postComment.getUpdateAt();
        commentCount = postComment.getCommentCount();
        retwitCount = postComment.getRetwitCount();
        likeCount = postComment.getLikeCount();
        postId = postComment.getId();
        postCheck = false;
        loginMemberComment = isLoginMember(loginId, postComment.getMember().getId());

        // 추가 정보
        likeCheck = isCheckByLike(postComment.getLikes(), loginId);
        retwitCheck = isCheckByRetwit(postComment.getRetwits(), loginId);
        fUpdateAt = formattingTime(postComment.getUpdateAt());

        if(postComment.getChildComment() != null){
            for(PostComment CommentChild : postComment.getChildComment()){
                if(CommentChild.getMember().equals(postMember)){
                    ownerComment = new DetailCommentDto(CommentChild, loginId, postMember);
                }
            }
        }
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
