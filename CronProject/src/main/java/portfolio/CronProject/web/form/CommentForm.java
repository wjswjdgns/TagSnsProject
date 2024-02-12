package portfolio.CronProject.web.form;

import lombok.Data;

@Data
public class CommentForm {
    private String content;
    private Long postId; // 포스트 ID가 있는지 없는지에 따라서 대댓글을 구분
    private Long commentId; // 답글 ID가 있는지 없는지 확인

}
