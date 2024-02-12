package portfolio.CronProject.web.dto;

import lombok.Data;
import portfolio.CronProject.domain.UserTag;

@Data
public class TagsDto {

    private Long tagId;
    private String tagName;
    public TagsDto(UserTag userTag) {
        tagId = userTag.getId();
        tagName = userTag.getTagName();
    }
}
