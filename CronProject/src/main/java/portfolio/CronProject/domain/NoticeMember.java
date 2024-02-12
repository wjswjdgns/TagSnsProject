package portfolio.CronProject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class NoticeMember {
    @Id
    @GeneratedValue
    @Column(name = "NoticeMemberId")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="UserId")
    private Member member;
    private Long activeMemberId;
    private Integer noticeSet; // 1 : 모든 글쓰기 2: 모든 글쓰기 & 댓글
}
