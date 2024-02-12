package portfolio.CronProject.web.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import portfolio.CronProject.domain.Post;
import portfolio.CronProject.domain.PostComment;
import portfolio.CronProject.domain.Retwit;

import java.time.LocalDateTime;

import static portfolio.CronProject.service.PostService.generateImageUrl;

@Data
public class MainDto {
    /**
     * 댓글, 포스트, 리트윗들이 해당 Dto에 들어오게 된다.
     * 포스트와 리트윗의 경우 부모 회원 정보, 부모 글 정보가 null값이 되어야 한다.
     * */

    // 회원 정보
    private String nickName;
    private String personal;
    private String profileImg;

    // 글 정보
    private Long id;
    private String TagName;
    private String TagMemberPersonal;
    private String content;
    private String imgUrl;
    private LocalDateTime updateAt;
    private String fUpdateAt; // 포맷팅 된 시간

    // 부모 회원 정보
    private String Par_nickName;
    private String Par_personal;
    private String Par_profileImg;

    // 부모 글 정보
    private Long Par_id;
    private String Par_TagName;
    private String Par_TagMemberPersonal;
    private String Par_content;
    private String Par_imgUrl;
    private LocalDateTime Par_updateAt;
    private String Par_fUpdateAt;

    // 카운트 정보
    private Integer commentCount;
    private Integer retwitCount;
    private Integer likeCount;

    private Integer Par_commentCount;
    private Integer Par_retwitCount;
    private Integer Par_likeCount;


    // 체크 정보 ( 로그인 회원이 좋아요 눌렀는지 안눌렀는지)
    private boolean likeCheck;
    private boolean retwitCheck;

    private boolean Par_likeCheck;
    private boolean Par_retwitCheck;


    // 체크 정보 (한개의 DTO값이 어디에서 온 것인지 확인)
    private String status;
    private boolean isPost;
    private boolean Par_isPost;

    // 추가 정보
    private String retwitMember; // 리트윗 멤버 정보
    private boolean loginMemberPost; // 로그인 한 멤버가 작성한 포스트인지 확인
    private boolean Par_loginMemberPost; // 로그인 한 멤버가 작성한 포스트인지 확인 -> 부모 글

    // 포스트 생성자
    public MainDto(Post post){

        // 회원 정보
        nickName = post.getMember().getNickName();
        personal = post.getMember().getPersonal();
        profileImg = post.getMember().getProfileImg();

        // 글 정보
        id = post.getId();
        if(post.getUserTag() != null){
            TagName = post.getUserTag().getTagName();
            TagMemberPersonal = post.getUserTag().getMember().getPersonal();
        }

        content = post.getContent();
        imgUrl = generateImageUrl(post);
        updateAt = post.getUpdateAt();

        // Count 정보
        commentCount = post.getCommentCount();
        retwitCount = post.getRetwitCount();
        likeCount = post.getLikeCount();

        // 부모 회원 정보
        // 부모 글 정보

        // 체크 정보 ( 로그인 회원이 좋아요 눌렀는지 안눌렀는지)

        // 체크 정보 (한개의 DTO값이 어디에서 온 것인지 확인)
        status = "post";
        isPost = true;
    }

    // 댓글 생성자
    public MainDto(PostComment postComment, boolean check){

        // 회원 정보
        nickName = postComment.getMember().getNickName();
        personal = postComment.getMember().getPersonal();
        profileImg = postComment.getMember().getProfileImg();

        // 글 정보
        id = postComment.getId();
        content = postComment.getCommentText();
        imgUrl = generateImageUrl(postComment);
        updateAt = postComment.getUpdateAt();

        // Count 정보
        commentCount = postComment.getCommentCount();
        retwitCount = postComment.getRetwitCount();
        likeCount = postComment.getLikeCount();

        // 부모 회원 정보
        if(check){
            Par_nickName = postComment.getPost().getMember().getNickName();
            Par_personal = postComment.getPost().getMember().getPersonal();
            Par_profileImg = postComment.getPost().getMember().getProfileImg();
        }
        else{
            Par_nickName = postComment.getParentComment().getMember().getNickName();
            Par_personal = postComment.getParentComment().getMember().getPersonal();
            Par_profileImg = postComment.getParentComment().getMember().getProfileImg();
        }

        // 부모 글 정보
        if(check){
            Par_id = postComment.getPost().getId();
            Par_content = postComment.getPost().getContent();
            Par_imgUrl = generateImageUrl(postComment.getPost());
            Par_updateAt = postComment.getPost().getUpdateAt();

            if(postComment.getPost().getUserTag() != null){
                Par_TagName = postComment.getPost().getUserTag().getTagName();
                Par_TagMemberPersonal = postComment.getPost().getUserTag().getMember().getPersonal();
            }

            Par_commentCount = postComment.getPost().getCommentCount();
            Par_retwitCount = postComment.getPost().getRetwitCount();
            Par_likeCount = postComment.getPost().getLikeCount();

            Par_isPost = true;
        }
        else{
            Par_id = postComment.getParentComment().getId();
            Par_content = postComment.getParentComment().getCommentText();
            Par_imgUrl = generateImageUrl(postComment.getParentComment());
            Par_updateAt = postComment.getParentComment().getUpdateAt();

            Par_commentCount = postComment.getParentComment().getCommentCount();
            Par_retwitCount = postComment.getParentComment().getRetwitCount();
            Par_likeCount = postComment.getParentComment().getLikeCount();

            Par_isPost = false;
        }

        // 체크 정보 ( 로그인 회원이 좋아요 눌렀는지 안눌렀는지)

        // 체크 정보 (한개의 DTO값이 어디에서 온 것인지 확인)
        status = "comment";
        isPost = false;
    }

    // 리트윗 생성자
    public MainDto(Retwit retwit, boolean check){

        if(check){
            // 회원 정보
            nickName = retwit.getPost().getMember().getNickName();
            personal = retwit.getPost().getMember().getPersonal();
            profileImg = retwit.getPost().getMember().getProfileImg();

            // 글 정보
            id = retwit.getPost().getId();

            if(retwit.getPost().getUserTag() != null){
                TagName = retwit.getPost().getUserTag().getTagName();
                TagMemberPersonal = retwit.getPost().getUserTag().getMember().getPersonal();
            }

            content = retwit.getPost().getContent();
            imgUrl = generateImageUrl(retwit.getPost());

            // Count 정보
            commentCount = retwit.getPost().getCommentCount();
            retwitCount = retwit.getPost().getRetwitCount();
            likeCount = retwit.getPost().getLikeCount();

            // 부모 회원 정보
            // 부모 글 정보

            isPost = true;
        }
        else{
            // 회원 정보
            nickName = retwit.getPostComment().getMember().getNickName();
            personal = retwit.getPostComment().getMember().getPersonal();
            profileImg = retwit.getPostComment().getMember().getProfileImg();

            // 글 정보
            id = retwit.getPostComment().getId();
            content = retwit.getPostComment().getCommentText();
            imgUrl = generateImageUrl(retwit.getPostComment());

            // Count 정보
            commentCount = retwit.getPostComment().getCommentCount();
            retwitCount = retwit.getPostComment().getRetwitCount();
            likeCount = retwit.getPostComment().getLikeCount();

            isPost = false;
        }

        updateAt = retwit.getCreateAt();

        // 체크 정보 ( 로그인 회원이 좋아요 눌렀는지 안눌렀는지)
        // 체크 정보 (한개의 DTO값이 어디에서 온 것인지 확인)
        status = "retwit";

        // 리트윗 추가 정보
        retwitMember = retwit.getMember().getNickName();
    }


}
