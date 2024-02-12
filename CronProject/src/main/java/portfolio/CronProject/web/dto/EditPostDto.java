package portfolio.CronProject.web.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import portfolio.CronProject.domain.Post;
import portfolio.CronProject.domain.PostComment;

import static portfolio.CronProject.service.PostService.generateImageUrl;

@Data
public class EditPostDto {
    private Long postId;
    private String content;
    private String imgUrl;
    private boolean isPost;
    private boolean useTag;
    private Long tagId;
    private String tagName;
    private String tagOwner;

    public EditPostDto(Object obj) {

        if(obj instanceof Post){
            postId = ((Post) obj).getId();
            content = ((Post) obj).getContent();
            imgUrl = generateImageUrl(((Post) obj));
            isPost = true;

            if(((Post) obj).getUserTag() != null){
                useTag = true;
                tagId = ((Post) obj).getUserTag().getId();
                tagName = ((Post) obj).getUserTag().getTagName();
                tagOwner = ((Post) obj).getMember().getPersonal();
            }
            else{
                useTag = false;
            }

        }
        else{
            postId = ((PostComment) obj).getId();
            content = ((PostComment) obj).getCommentText();
            imgUrl = generateImageUrl(((PostComment) obj));
            isPost = false;
            useTag = false;
        }
    }

}


