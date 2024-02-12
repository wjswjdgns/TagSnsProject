package portfolio.CronProject.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PostImage {
    @Id
    @GeneratedValue
    @Column(name = "PostImageId")
    private Long id;

    private String uploadFileName;
    private String storeFileName; // UUID로 저장 할 수 있도록 한다.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="UserId")
    private Member member;

    @OneToOne(mappedBy = "postImage", fetch = FetchType.LAZY)
    private Post post;

    @OneToOne(mappedBy = "postImage", fetch = FetchType.LAZY)
    private PostComment postComment;
}
