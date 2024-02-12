package portfolio.CronProject.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import portfolio.CronProject.domain.UserTag;

import java.util.ArrayList;
import java.util.List;

@Data
public class UpdateForm {
    private String nickName;
    private String introduce;
    private List<createTag> createTaglist;

    @Data
    public static class createTag{
        private String tagName;
        private int tagCount;
        private int weekCount;
    }
}
