package portfolio.CronProject.web.dto;


import lombok.Data;
import java.util.List;

@Data
public class NotificationSettingDto {
    private String personal;
    private boolean NoPost;
    private boolean allPost;
    private boolean allPostComment;
    private List<tagListDto> tagList;

    public NotificationSettingDto(String personal, Integer notificationStatus, List<tagListDto> tagListDtos) {
        this.personal = personal;
        // 모든글쓰기
        if(notificationStatus ==  1){
            NoPost = false;
            allPost = true;
            allPostComment = false;
        }
        // 모든 글쓰기와 댓글
        else if(notificationStatus ==  2){
            NoPost = false;
            allPost = false;
            allPostComment = true;
        }
        // 알림 받지 않음
        else{
            NoPost = true;
            allPost = false;
            allPostComment = false;
        }

        tagList = tagListDtos;
    }

}
