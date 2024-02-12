package portfolio.CronProject.web.dto;

import lombok.Data;
import portfolio.CronProject.domain.Post;
import portfolio.CronProject.domain.PostComment;

import static portfolio.CronProject.service.PostService.generateImageUrl;

@Data
public class PrevPostDto {
    private Long postId;
    private String nickName;
    private String personal;
    private String profileImg;
    private String content;
    private String imgUrl;

    public PrevPostDto(Long postId, String nickName, String personal, String profileImg, String content, Object obj) {
        this.postId = postId;
        this.nickName = nickName;
        this.personal = personal;
        this.profileImg = profileImg;
        this.content = content;
        imgUrl = generateImageUrl(obj);
    }

}
