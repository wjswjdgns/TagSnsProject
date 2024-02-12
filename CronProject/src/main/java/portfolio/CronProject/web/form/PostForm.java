package portfolio.CronProject.web.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostForm {
    private Long tagId;
    private String content;
    private MultipartFile image;
}
