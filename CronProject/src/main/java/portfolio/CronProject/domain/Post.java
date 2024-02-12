package portfolio.CronProject.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// post에서 연관관계의주인을 지정
@Entity
@Table(name = "posts")
@Getter @Setter
public class Post {

    @Id @GeneratedValue
    @Column(name = "PostId")
    private Long id;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Integer likeCount;
    private Integer retwitCount;
    private Integer commentCount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="UserId")
    private Member member;
    @OneToMany(mappedBy = "post")
    private List<PostComment> postComments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Retwit> retwits = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Notice> notice = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="UserTagId")
    private UserTag userTag;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PostImageId")
    private PostImage postImage;

    /* 비즈니스 로직 */
    /* 좋아요, 리트윗, 댓글 갯수 늘리기 */
    public void addStock(String state){
        if(state.equals("like")){
            this.likeCount += 1;
        }
        else if(state.equals("retwit")){
            this.retwitCount += 1;
        }
        else if(state.equals("comment")){
            this.commentCount += 1;
        }
    }

    /* 좋아요, 리트윗, 댓글 갯수 줄이기 */
    public void removeStock(String state){
        if(state.equals("like")){
            this.likeCount -= 1;
        }
        else if(state.equals("retwit")){
            this.retwitCount -= 1;
        }
        else if(state.equals("comment")){
            this.commentCount -= 1;
        }
    }



}
