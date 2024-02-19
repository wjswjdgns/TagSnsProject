package portfolio.CronProject.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import portfolio.CronProject.domain.PostComment;
import portfolio.CronProject.domain.PostImage;

@Repository
@RequiredArgsConstructor
public class ImageRepository {

    private final EntityManager em;

    // 이미지 업로드 하기
    public void saveImage(PostImage postImage) {
        em.persist(postImage);
    }

    // 이미지 가져오기
    public PostImage findPostImage(Long postImageId){
        return em.find(PostImage.class, postImageId);
    }

}
