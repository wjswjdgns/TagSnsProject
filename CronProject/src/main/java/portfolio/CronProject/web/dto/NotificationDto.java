package portfolio.CronProject.web.dto;

import lombok.Data;
import portfolio.CronProject.domain.Notice;

@Data
public class NotificationDto {
    private Long postId;
    private boolean postCheck;
    private String personal;
    private String profileImg;
    private Integer status;
    private String content;
    private String userTagName;

    public NotificationDto(Notice notice) {
        personal = notice.getActivePersonal();
        profileImg = notice.getActiveProfileImg();
        status = notice.getNoticeStatus();

        if(notice.getPost() != null){
            postId = notice.getPost().getId();
            postCheck = true;
            content = notice.getPost().getContent();
        }
        else{
            postId = notice.getPostComment().getId();
            postCheck = false;
            content = notice.getPostComment().getCommentText();
        }

        // 쿼리문 확인 필요
        if(notice.getUserTag() != null){
            userTagName = notice.getUserTag().getTagName();
        }
    }
}
