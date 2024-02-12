package portfolio.CronProject.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import portfolio.CronProject.domain.Member;
import portfolio.CronProject.domain.PostImage;
import portfolio.CronProject.repository.ImageRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    @Value("${server.servlet.multipart.location}")
    private Resource uploadDir;

    // DB에 저장하기
    public Long saveDB(String uploadFileName, String storeFileName, Member member){

        PostImage postImage = new PostImage();
        postImage.setMember(member);
        postImage.setStoreFileName(storeFileName);
        postImage.setUploadFileName(uploadFileName);
        imageRepository.saveImage(postImage);

        return postImage.getId();
    }

    // 서버 디렉토리에 있는 포스트 이미지 삭제
    public void deleteImage(String storeFileName){
        Path resolvePath = getResourcePath(uploadDir).resolve("postImage");
        deleteImage(resolvePath,storeFileName);
    }


    // 서버 디렉토리에 포스트 이미지를 저장 (저장한 파일 이름을 반환)
    public String saveImage(MultipartFile image) throws IOException {
        Path uploadPath = getResourcePath(uploadDir).resolve("postImage");

        String uuid = UUID.randomUUID().toString(); // 서버에 파일 이름을 저장하기 위해 UUID 구성
        String fileName = uuid + "_" + Normalizer.normalize(image.getOriginalFilename(), Normalizer.Form.NFC);

        Path filePath = uploadPath.resolve(fileName);

        try{
            // 파일 저장
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException e){
            throw new RuntimeException("파일을 저장할 수 없습니다. 다시 시도해주세요", e);
        }

        return fileName;
    }


    // 서버 디렉토리에 프로필 이미지 등록하기
    public String saveProfileImage(MultipartFile image, String nickName, String oldImageFileName)  throws IOException{
        Path uploadPath = getResourcePath(uploadDir).resolve("profileImg");

        // 이전 이미지 삭제하기
        // oldImageFileName이 null 값인 경우는 한번도 프로필 변경이 일어나지 않은 사람의 경우다
        if(oldImageFileName != null && !oldImageFileName.equals("default_profile.png")){
            deleteImage(uploadPath,oldImageFileName);
        }

        String uuid = UUID.randomUUID().toString(); // 서버에 파일 이름을 저장하기 위해 UUID 구성
        String fileName = uuid + "_" + nickName + "_" + Normalizer.normalize(image.getOriginalFilename(), Normalizer.Form.NFC);

        Path filePath = uploadPath.resolve(fileName);

        try{
            // 파일 저장
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException e){
            throw new RuntimeException("파일을 저장할 수 없습니다. 다시 시도해주세요", e);
        }

        return fileName;
    }


    // 서버 디렉토리에 백그라운드 이미지 등록하기
    public String saveBackgroundImage(MultipartFile image, String nickName, String oldImageFileName)  throws IOException{
        Path uploadPath = getResourcePath(uploadDir).resolve("BackgroundImg");

        // 이전 이미지 삭제하기
        // oldImageFileName이 null 값인 경우는 한번도 프로필 변경이 일어나지 않은 사람의 경우다
        if(oldImageFileName != null && !oldImageFileName.equals("default_background.png")){
            deleteImage(uploadPath,oldImageFileName);
        }

        String uuid = UUID.randomUUID().toString(); // 서버에 파일 이름을 저장하기 위해 UUID 구성
        String fileName = uuid + "_" + nickName + "_" + Normalizer.normalize(image.getOriginalFilename(), Normalizer.Form.NFC);

        Path filePath = uploadPath.resolve(fileName);

        try{
            // 파일 저장
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException e){
            throw new RuntimeException("파일을 저장할 수 없습니다. 다시 시도해주세요", e);
        }

        return fileName;
    }

    private void deleteImage(Path path,String fileName){
        try{
            Path imagePath = path.resolve(fileName);
            Files.deleteIfExists(imagePath);
        }catch(IOException e){
            // 예외 처리
            e.printStackTrace();
            throw new RuntimeException("이미지 삭제 중 에러 발생");
        }
    }

    private Path getResourcePath(Resource resource) {
        try {
            // 클래스패스 리소스를 해석하여 파일 시스템 경로로 변환
            return Path.of(ResourceUtils.getFile(resource.getURI()).getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("리소스를 파일 시스템 경로로 변환할 수 없습니다.", e);
        }
    }

}
