package portfolio.CronProject.web.dto;

import lombok.Data;
import portfolio.CronProject.domain.UserTag;

@Data
public class SearchTagDto {
    private Long tagId;
    private String personal;
    private String profileImg;
    private String tagName;
    private Integer tagCount;

    public SearchTagDto(UserTag userTag) {
        tagId = userTag.getId();
        personal = userTag.getMember().getPersonal();
        profileImg = userTag.getMember().getProfileImg();
        tagName = userTag.getTagName();
        tagCount = userTag.getTagCount();
    }
}
