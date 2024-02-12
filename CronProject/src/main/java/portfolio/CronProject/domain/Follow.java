package portfolio.CronProject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Follow {

    @Id
    @GeneratedValue
    @Column(name = "FollowId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="UserId")
    private Member member; // 팔로우 주인

    private Long followMemberId; // 대상 팔로우 아이디
}
