package portfolio.CronProject.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class UserTag {

    @Id
    @GeneratedValue
    @Column(name = "UserTagId")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="UserId")
    private Member member;

    @OneToMany(mappedBy = "userTag", cascade = CascadeType.REMOVE)
    private List<NoticeTag> noticeTag = new ArrayList<>(); // 여기서 부터 시작

    @OneToMany(mappedBy = "userTag", cascade = CascadeType.REMOVE)
    private List<Notice> notice = new ArrayList<>();

    @OneToMany(mappedBy = "userTag", cascade = CascadeType.REMOVE)
    private List<Post> post = new ArrayList<>();

    private String tagName;
    private Integer tagCount;
    private Integer weekCount;


    /*비즈니스 로직*/
    /*태그 사용 횟수 증가*/
    public void addStock(){
        tagCount += 1;
    }
    public void removeStock(){
        tagCount -= 1;
    }

}
