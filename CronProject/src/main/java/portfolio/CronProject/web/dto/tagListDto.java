package portfolio.CronProject.web.dto;

import lombok.Data;
import portfolio.CronProject.domain.UserTag;

@Data
public class tagListDto {
    private Long tagId;
    private String tagName;
    private boolean check;

    public tagListDto(UserTag userTag) {
        tagId = userTag.getId();
        tagName = userTag.getTagName();
    }
}
