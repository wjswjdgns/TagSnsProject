package portfolio.CronProject.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class PostComment {

    @Id @GeneratedValue
    @Column(name="CommentId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="UserId")
    private Member member;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PostId")
    private Post post;

    @OneToMany(mappedBy = "postComment", cascade = CascadeType.REMOVE)
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "postComment", cascade = CascadeType.REMOVE)
    private List<Retwit> retwits = new ArrayList<>();

    @OneToMany(mappedBy = "postComment", cascade = CascadeType.REMOVE)
    private List<Notice> notice = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ParCommentId")
    private PostComment parentComment; // 부모 댓글
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.REMOVE)
    private List<PostComment> childComment;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PostImageId")
    private PostImage postImage;

    private String commentText;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    private Integer likeCount;
    private Integer retwitCount;
    private Integer commentCount;


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
