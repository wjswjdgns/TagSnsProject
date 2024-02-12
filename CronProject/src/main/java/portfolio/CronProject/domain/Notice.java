package portfolio.CronProject.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Notice {
    @Id
    @GeneratedValue
    @Column(name = "NoticeId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="UserId")
    private Member member;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PostId")
    private Post post;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CommentId")
    private PostComment postComment;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="UserTagId")
    private UserTag userTag;

    private String ActivePersonal;
    private String ActiveProfileImg;

    // 받은 알림 종류
    // 1 : 글쓰기 알림 , 2 : 좋아요 알림, 3 : 리트윗 알림, 4 댓글 알림, 5 태그 알림
    private Integer noticeStatus;
    private LocalDateTime createAt; // 알림 받은 시간

}
