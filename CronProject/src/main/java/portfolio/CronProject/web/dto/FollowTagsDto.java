package portfolio.CronProject.web.dto;

import lombok.Data;
import portfolio.CronProject.domain.UserTag;

import java.util.List;

@Data
public class FollowTagsDto {
    private String nickName;
    private String personal;
    private String profileImg;
    private List<TagsDto> tagList;

    public FollowTagsDto(String nickName, String personal, String profileImg) {
        this.nickName = nickName;
        this.personal = personal;
        this.profileImg = profileImg;
    }
}
