package portfolio.CronProject.web;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import portfolio.CronProject.domain.Member;
import portfolio.CronProject.domain.Post;
import portfolio.CronProject.domain.PostComment;
import portfolio.CronProject.service.*;
import portfolio.CronProject.web.argumentresolver.Login;
import portfolio.CronProject.web.dto.DetailPostDto;
import portfolio.CronProject.web.dto.EditPostDto;
import portfolio.CronProject.web.dto.FollowTagsDto;
import portfolio.CronProject.web.dto.PrevPostDto;
import portfolio.CronProject.web.form.ComposePostForm;

import java.io.IOException;
import java.text.Normalizer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final MemberService memberService;
    private final PostService postService;
    private final CommentService commentService;
    private final NotificationsService notificationsService;
    private final ImageService imageService;
    private final TagService tagService;

    // 상세 페이지 접근하기
    @GetMapping("/{personal}/post/{postId}")
    public String detailPost(@PathVariable("postId") Long postId,
                             @Login Member loginMember, Model model){

        // 서비스에서 새로운 Dto 를 받아서 해당 내용을 뿌려준다.
        DetailPostDto detailPost = postService.getDetailPost(loginMember.getId(), postId);
        model.addAttribute("post", detailPost);

        // 포스트 서비스에서 상세 내용을 담을 수 있도록 한다. (포스트 작성 멤버 정보, 포스트 정보, 댓글 정보)
        return "basic/detail";
    }

    // 댓글 상세페이지 접근하기
    @GetMapping("/{personal}/comment/{postId}")
    public String detailComment(@PathVariable("postId") Long postId,
                             @Login Member loginMember, Model model){

        // 서비스에서 새로운 Dto 를 받아서 해당 내용을 뿌려준다.
        DetailPostDto detailPost = postService.getDetailComment(loginMember.getId(), postId);
        model.addAttribute("post", detailPost);

        // 포스트 서비스에서 상세 내용을 담을 수 있도록 한다. (포스트 작성 멤버 정보, 포스트 정보, 댓글 정보)
        return "basic/detail";
    }

    // 글 수정 페이지 이동
    @PostMapping("/compose/edit")
    public String editPost(@ModelAttribute ComposePostForm composePostForm, @Login Member loginMember, Model model){

        // 로그인 회원과 동일하지 않은 경우 에러 발생
        Member member = memberService.findOne(loginMember.getId());
        model.addAttribute("member", member);

        // post나 comment를 불러온다.
        EditPostDto editPost = postService.getEditPost(composePostForm.getPostId(), composePostForm.isPostCheck());
        model.addAttribute("post", editPost);

        // writeStatus (default)
        // 글쓰기의 상태값을 넣어준다.
        String writeStatus = "editPost";
        model.addAttribute("writeStatus", writeStatus);

        if(editPost.isPost()){
            // 태그 값이 들어간다. (자신이 팔로우 한 사람의 Tag가 들어간다)
            List<FollowTagsDto> followTagList = tagService.getFollowTagList(loginMember.getId());
            model.addAttribute("followTagList", followTagList);
        }

        return "basic/write";
    }

    // 글 수정하기
    @ResponseBody
    @PostMapping("/write/edit")
    public void editPost(@RequestParam(value = "image",required = false) MultipartFile image,
                         @RequestParam String content,
                         @RequestParam(value = "tagId", required = false) Long tagId,
                         @RequestParam Long postId,
                         @RequestParam boolean postCheck,
                         @RequestParam(value = "checkImage") boolean checkImage,
                         @Login Member loginMember) throws IOException {

        Member member = memberService.findOne(loginMember.getId());
        Long postImageId = null;

        // 이미지 처리
        if(image != null && !image.isEmpty()){
            // 새로운 이미지를 서버에 등록
            String storeFileName = imageService.saveImage(image);
            postImageId = imageService.saveDB(Normalizer.normalize(image.getOriginalFilename(), Normalizer.Form.NFC), storeFileName, member);
        }

        // 포스트 수정 처리
        if(postCheck){

            // 이미지가 없고 기존 포스터에 이미지가 있는 경우
            if(!checkImage && postService.getPostOne(postId).getPostImage() != null){
                // 기존의 사진을 삭제하는 경우 삭제처리를 진행한다.
                String OldstoreFileName = postService.getPostOne(postId).getPostImage().getStoreFileName();
                imageService.deleteImage(OldstoreFileName);
            }
            postService.editPost(postId, content, tagId, postImageId, checkImage);
        }
        else{
            if(!checkImage && commentService.getCommentOne(postId).getPostImage() != null){
                // 기존의 사진을 삭제하는 경우 삭제처리를 진행한다.
                String OldstoreFileName = commentService.getCommentOne(postId).getPostImage().getStoreFileName();
                imageService.deleteImage(OldstoreFileName);
            }
            commentService.editPost(postId, content,postImageId, checkImage);
        }

    }


    // 글 삭제하기
    @ResponseBody
    @PostMapping("/write/delete")
    public void deletePost(@RequestBody ComposePostForm composePostForm){

        // 포스트 였을 경우
        if(composePostForm.isPostCheck()){
            postService.deletePost(composePostForm.getPostId());
        }
        // 댓글이었을 경우
        else{
            commentService.deleteComment(composePostForm.getPostId());
        }

    }


    // 포스트 등록하기
    @ResponseBody
    @PostMapping("/write/default")
    public void write(@RequestParam(value = "image",required = false) MultipartFile image,
                      @RequestParam String content,
                      @RequestParam(value = "tagId", required = false) Long tagId,
                      @Login Member loginMember) throws IOException {

        Member member = memberService.findOne(loginMember.getId());

        Long postImageId = null;

        // 이미지 처리
        if(image != null && !image.isEmpty()){
            // 이미지가 업로드 된 경우 이미지 처리
            // Service를 통해서 데이터를 서버 directory에 저장하고 해당 경로를 데이터베이스에 저장
            // 해당 Id 값을 Return 받는다.

            // 서버 디렉토리에 임시 저장 ( 추후 이미지 서버를 활용한다면 그곳으로 경로 이동 )
            String storeFileName = imageService.saveImage(image);

            // 데이터 데이스에 저장
            postImageId = imageService.saveDB(Normalizer.normalize(image.getOriginalFilename(), Normalizer.Form.NFC), storeFileName, member);
        }


        // 포스트 등록 태그가 없는 경우
        // 만약에 태그가 지정이 되었다면 태그 사용 수를 늘린다?
        Long postId = postService.post(member, content, tagId, postImageId);// 포스트 등록

        // 알림 등록
        notificationsService.savePostNotification(member.getId(), postId,true,1); // 사용자 글쓰기

        // 태그 알림 등록
        if(tagId != null){
            notificationsService.saveTagNotification(postId, tagId); // 태그 글쓰기
        }

    }

    // 댓글 등록하기
    @ResponseBody
    @PostMapping("/write/comment")
    public void comment(@RequestParam(value = "image",required = false) MultipartFile image,
                        @RequestParam String content,
                        @RequestParam(value = "Par_postId",required = false) Long Par_postId,
                        @RequestParam(value = "Par_commentId",required = false) Long Par_commentId,
                        @Login Member loginMember) throws IOException {
        Member member = memberService.findOne(loginMember.getId());
        Long commentId;
        Long postImageId = null;


        // 이미지 처리
        if(image != null && !image.isEmpty()){
            // 이미지가 업로드 된 경우 이미지 처리
            // Service를 통해서 데이터를 서버 directory에 저장하고 해당 경로를 데이터베이스에 저장
            // 해당 Id 값을 Return 받는다.

            // 서버 디렉토리에 임시 저장 ( 추후 이미지 서버를 활용한다면 그곳으로 경로 이동 )
            String storeFileName = imageService.saveImage(image);

            // 데이터 데이스에 저장
            postImageId = imageService.saveDB(Normalizer.normalize(image.getOriginalFilename(), Normalizer.Form.NFC), storeFileName, member);
        }

        // 댓글인 경우
        if(Par_postId != null){
            Post post = postService.getPostOne(Par_postId);
            commentId = commentService.comment(member, post, content, postImageId);
        }
        // 대댓글인 경우
        else{
            PostComment comment = commentService.getCommentOne(Par_commentId);
            commentId = commentService.recomment(member, comment, content, postImageId);
        }

        notificationsService.savePostNotification(member.getId(), commentId,false,2); // 알림 등록
    }

    // 일반 글쓰기
    @GetMapping("/compose/default")
    public String defaultPost(@Login Member loginMember, Model model){

        Member member = memberService.findOne(loginMember.getId());
        model.addAttribute("member", member);

        // 글쓰기의 상태값을 넣어준다.
        String writeStatus = "default";
        model.addAttribute("writeStatus", writeStatus);

        // 태그 값이 들어간다. (자신이 팔로우 한 사람의 Tag가 들어간다)
        List<FollowTagsDto> followTagList = tagService.getFollowTagList(loginMember.getId());
        model.addAttribute("followTagList", followTagList);

        return "basic/write";
    }

    // 답글 쓰기
    @PostMapping("/compose/comment")
    public String commentPost(@Login Member loginMember, Long postId, String check, Model model){

        Member member = memberService.findOne(loginMember.getId());
        model.addAttribute("member", member);

        boolean checkPost;

        if(check.equals("true")){
            checkPost = true;
        }
        else{
            checkPost = false;
        }

        PrevPostDto post = postService.getPrevPost(postId, checkPost);
        model.addAttribute("post", post);

        model.addAttribute("check", checkPost);

        // 글쓰기의 상태값을 넣어준다.
        String writeStatus = "prevPost";
        model.addAttribute("writeStatus", writeStatus);

        return "basic/write";
    }

}
