package portfolio.CronProject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.CronProject.domain.*;
import portfolio.CronProject.repository.*;
import portfolio.CronProject.web.dto.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final RetwitRepository retwitRepository;
    private final FollowRepository followRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final TagRepository tagRepository;
    private final ImageRepository imageRepository;

    /**
     * 등록하기
     * */
    public Long post(Member member, String postWord, Long TagId, Long postImageId){

        Post post = new Post();
        post.setMember(member); // 등록하는 사람
        post.setContent(postWord); // 등록 할 글

        if(TagId != null){
            UserTag tag = tagRepository.findTag(TagId);
            post.setUserTag(tag);
            tag.addStock();
        }

        if(postImageId != null){
            PostImage postImage = imageRepository.findPostImage(postImageId);
            post.setPostImage(postImage);
        }

        post.setCreateAt(LocalDateTime.now()); // 등록 시간
        post.setUpdateAt(LocalDateTime.now()); // 수정 시간
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setRetwitCount(0);

        postRepository.save(post);
        return post.getId();
    }

    /**
     * 수정하기
     * */
    public void editPost(Long postId, String content, Long TagId,Long postImageId, boolean checkImage){
        Post post = postRepository.findPost(postId);
        post.setContent(content);

        // 태그로 처리해야 하는 것
            // 기존에 있는데 삭제하는 경우 (post.getUserTag() != null , post.getagId = null)
            // 기존에 있던 태그를 다른 것으로 변경 했을 경우 (post.getUserTag() != null , post.getagId != null)
            // 기존에 없던 태그를 새롭게 추가 했을 경우 (post.getUserTag() == null , post.getagId != null)
        if(TagId != null){

            // 기존 태그와 같지 않은 경우에만 변경 시작
            if(post.getUserTag() != null){
                if(post.getUserTag().getId() != TagId){
                    // 기존 스톡을 지우고
                    UserTag userTag = post.getUserTag();
                    userTag.removeStock();

                    // 새롭게 변경 후 스톡을 늘려준다.
                    UserTag tag = tagRepository.findTag(TagId);
                    post.setUserTag(tag);
                    tag.addStock();
                }
            }

            // 기존에 태그가 없다면 추가한다.
            else{
                UserTag tag = tagRepository.findTag(TagId);
                post.setUserTag(tag);
                tag.addStock();
            }
        }
        else{
            post.setUserTag(null);
        }

        if(!checkImage && post.getPostImage() != null){
            post.setPostImage(null);
        }

        // 수정 시 이미지 삭제를 생각해야 하는구나
        if(postImageId != null){
            PostImage postImage = imageRepository.findPostImage(postImageId);
            post.setPostImage(postImage);
        }

        post.setUpdateAt(LocalDateTime.now()); // 수정 시간 체크
    }


    /**
     * Post 찾기
     * */
    public Post getPostOne(Long postId){
        return postRepository.findPost(postId);
    }

    /**
     * Post 삭제하기
     * **/
    public void deletePost(Long postId){
        postRepository.deletePost(postId);
    }



    /**
     * 글 수정 정보 전달하기
     * */

    public EditPostDto getEditPost(Long postId, boolean check){
        EditPostDto editPostDto = null;
        if(check){
            Post post = postRepository.findPost(postId);
            editPostDto = new EditPostDto(post);
        }
        else{
            PostComment comment = commentRepository.findComment(postId);
            editPostDto = new EditPostDto(comment);
        }
        return editPostDto;
    }


    /**
     * 상세페이지 정보 전달하기 (포스트 클릭)
     * */
    public DetailPostDto getDetailPost(Long loginId, Long postId){
        Post post = postRepository.findPost(postId);
        Member postMember = post.getMember();
        DetailPostDto detailPostDto = new DetailPostDto(post, loginId);

        // 해당 포스트의 댓글들 찾아 detailCommentDto 를 만들고
        List<PostComment> postComment = commentRepository.findPostComment(post);
        List<DetailCommentDto> detailCommentDto = postComment.stream()
                .map(o -> new DetailCommentDto(o, loginId, postMember))
                .collect(Collectors.toList());

        detailPostDto.setCommentList(detailCommentDto);
        return detailPostDto;
    }

    /**
     * 상세페이지 정보 전달하기 (댓글 클릭)
     * */

    public DetailPostDto getDetailComment(Long loginId, Long commentId){
        PostComment comment = commentRepository.findComment(commentId);
        Member commentMember  = comment.getMember();
        DetailPostDto detailPostDto = new DetailPostDto(comment, loginId);

        // 해당 댓글의 댓글을 찾아 detailCommentDto를 만든다
        List<PostComment> commentComment = commentRepository.findCommentComment(comment);
        List<DetailCommentDto> detailCommentDtos = commentComment.stream()
                .map(o -> new DetailCommentDto(o, loginId, commentMember))
                .collect(Collectors.toList());

        detailPostDto.setCommentList(detailCommentDtos);
        return detailPostDto;
    }



    /**
     *
     * 마이페이지 (댓글 노출)
     * Comment : 자신의 댓글 노출
     *
     * */
    public List<MainDto> getMypageCommentList(Long loginId){
        // 각 레포지토리에서 정보를 가져오기
        List<PostComment> commentList = commentRepository.ParfindCommentList(loginId);
        //Dto 만들기 & 보내기
        return getCommentDto(loginId, commentList);
    }


    /**
     *
     * 마이페이지 (댓글 노출)
     * Post & Comment & Retwit 좋아요 한 글 노출하기
     *
     **/
     public List<MainDto> getMypageLikeList(Long loginId){

         // 각 레포지토리에서 정보 가져오기
         List<Post> postList = postRepository.findLikeList(loginId);
         List<Retwit> retwitListPost = retwitRepository.findLikeListPost(loginId);
         List<Retwit> retwitListComment = retwitRepository.findLikeListComment(loginId);
         List<PostComment> commentList = commentRepository.findLikeList(loginId);

         //DTO 만들기
         List<MainDto> postDto = getPostDto(loginId, postList);
         List<MainDto> commentDto = getCommentDto(loginId, commentList);
         List<MainDto> retwitPostDto = getRetwitDto(loginId, retwitListPost);
         List<MainDto> retwitCommentDto = getRetwitDto(loginId, retwitListComment);

         // merge & 전달
         return mergeMainList(postDto,commentDto,retwitPostDto,retwitCommentDto);
     }

    /***
     *
     * 마이페이지 ( 포스트 노출 )
     * Post : 자신의 포스트 노출
     *
     */
    public List<MainDto> getMypagePostList(Long loginId){

        // 각 레포지토리에서 정보를 가져오기
        List<Post> postList = postRepository.findFollowPostListFiler(loginId);
        List<Retwit> retwitList = retwitRepository.findRetwitList(loginId);

        //DTO 만들기
        List<MainDto> postDto = getPostDto(loginId, postList);
        List<MainDto> retwitDto = getRetwitDto(loginId, retwitList);

        return mergeMainList(postDto,retwitDto);
    }


    /***
     *
     * 메인 화면 ( 포스트 노출 )
     * Post : 자신과 자신이 팔로잉 한 멤버의 포스트 노출 ( 최신 순 위로 노출 )
     * 댓글 : 자신과 자신이 팔로잉 한 멤버의 댓글만 노출 ( 단! 전체 가장 먼저 작성된 댓글을 노출 )
     *
     */

    public List<MainDto> getMainPostList(Long loginId){

        //팔로우 목록 꺼내기
        List<Follow> followMember = followRepository.findFollowingByMember(loginId);
        List<Member> followMemberList = new ArrayList<>(); // 해당 멤버의 Member 객체를 넣을 List 생성

        for(Follow follow : followMember){
            Long followMemberId = follow.getFollowMemberId();
            Member member = memberRepository.findOne(followMemberId);
            followMemberList.add(member);
        }

        // 각 레포지토리에서 정보를 가져오기
        List<Post> postList = postRepository.findFollowPostList(loginId,followMemberList);
        List<PostComment> commentList = commentRepository.findCommentListAll(loginId,followMemberList);
        List<Retwit> retwitList = retwitRepository.findRetwitListAll(followMemberList);


        //DTO 만들기
        List<MainDto> postDto = getPostDto(loginId, postList);
        List<MainDto> commentDto = getCommentDto(loginId, commentList);
        List<MainDto> retwitDto = getRetwitDto(loginId, retwitList);

        //3개를 전부 같은 DTO로 만들기
        // 포스트 DTO

        return mergeMainList(postDto,commentDto,retwitDto);
    }



    /***
     *
     * 메인 화면 ( 포스트 노출 )
     * Post : 팔로잉 한 멤버의 포스트 노출 ( 최신 순 위로 노출 )
     * 댓글 : 팔로잉 한 멤버의 댓글만 노출 ( 단! 전체 가장 먼저 작성된 댓글을 노출 )
     *
     */
    public List<MainDto> getMainSelectPostList(Long loginId, Long findId) {
        
        // 각 레포지토리에서 정보를 가져오기
        List<Post> postList = postRepository.findFollowPostListFiler(findId);
        List<PostComment> commentList = commentRepository.ParfindCommentList(findId);
        List<Retwit> retwitList = retwitRepository.findRetwitList(findId);

        //DTO 만들기
        List<MainDto> postDto = getPostDto(loginId, postList);
        List<MainDto> commentDto = getCommentDto(loginId, commentList);
        List<MainDto> retwitDto = getRetwitDto(loginId, retwitList);

        return mergeMainList(postDto,commentDto,retwitDto);
    }


    /***
     *
     * 검색 화면 ( 포스트 노출 )
     * Post : 검색어에 따른 포스트 노출 ( 최신 순 위로 노출 )
     *
     */
    public List<MainDto> getSearchPost(Long loginId, String search){
        List<Post> searchPost = null;


        // 잘못된 글씨가 나왔을 때 처리
        if(search.contains("#") && search.contains("@")){
            String[] searchTokens = search.split("\\s+"); // 공백기준으로 나누기

            String tag = null;
            String user = null;
            List<String> temp = new ArrayList<>();

            // 검색어 분류하기
            for (String token :searchTokens){
                if(token.startsWith("#") && tag == null){
                    tag = token.substring(1);
                } else if (token.startsWith("@") && user == null){
                    user = token.substring(1);
                }
                else{
                    temp.add(token);
                }
            }

            // #, @ 이외 다른 글이 있는 경우 일반 검색으로 변경
            if(!temp.isEmpty()){
                searchPost = postRepository.findSearchPost(search);
            }
            else if(tag != null && user != null){
                Member selectMember = memberRepository.findPersonal(user);
                UserTag selectUserTag = tagRepository.findSelectUserTag(selectMember, tag); // 유저 태그 확인
                searchPost = postRepository.findSearchSelectTagPost(selectUserTag);
            }
            // 정해진 규칙을 지치기 않았을 경우 일반 검색으로 진행
            else{
                searchPost = postRepository.findSearchPost(search);
            }
        }
        else if(search.charAt(0) == '#'){
            String TagName = search.substring(1);
            List<UserTag> selectTagList = tagRepository.findNameTag(TagName);
            searchPost = postRepository.findSearchTagPost(selectTagList);
        }
        else if(search.charAt(0) == '@'){
            String UserPersonal = search.substring(1);
            Member selectMember = memberRepository.findPersonal(UserPersonal);
            searchPost = postRepository.findFollowPostListFiler(selectMember.getId());
        }
        else{
            searchPost = postRepository.findSearchPost(search);
        }
        return getPostDto(loginId, searchPost);
    }


    /***
     * DTO 만들기
     * */
    private List<MainDto> getPostDto(Long loginId, List<Post> postList){
        List<MainDto> returnPostList = postList.stream()
                .map(o -> {
                    MainDto mainDto = new MainDto(o);
                    mainDto.setFUpdateAt(formattingTime(o.getUpdateAt()));
                    mainDto.setLikeCheck(isCheckByLike(o.getLikes(), loginId));
                    mainDto.setRetwitCheck(isCheckByRetwit(o.getRetwits(), loginId));
                    mainDto.setLoginMemberPost(checkLoginPost(loginId,o.getMember().getId()));
                    return mainDto;
                })
                .collect(Collectors.toList());

        return returnPostList;
    }

    private List<MainDto> getCommentDto(Long loginId, List<PostComment> commentList){
        List<MainDto> returnCommentList = commentList.stream()
                .map(o -> {
                    if (o.getPost() != null) {
                        MainDto mainDto = new MainDto(o, true);
                        mainDto.setFUpdateAt(formattingTime(o.getUpdateAt()));
                        mainDto.setPar_fUpdateAt(formattingTime(o.getPost().getUpdateAt()));
                        mainDto.setLikeCheck(isCheckByLike(o.getLikes(), loginId));
                        mainDto.setRetwitCheck(isCheckByRetwit(o.getRetwits(), loginId));
                        mainDto.setPar_likeCheck(isCheckByLike(o.getPost().getLikes(), loginId));
                        mainDto.setPar_retwitCheck(isCheckByRetwit(o.getPost().getRetwits(), loginId));
                        mainDto.setLoginMemberPost(checkLoginPost(loginId,o.getMember().getId()));
                        mainDto.setPar_loginMemberPost(checkLoginPost(loginId,o.getPost().getMember().getId()));
                        return mainDto;
                    } else {
                        MainDto mainDto = new MainDto(o, false);
                        mainDto.setFUpdateAt(formattingTime(o.getUpdateAt()));
                        mainDto.setPar_fUpdateAt(formattingTime(o.getParentComment().getUpdateAt()));
                        mainDto.setLikeCheck(isCheckByLike(o.getLikes(), loginId));
                        mainDto.setRetwitCheck(isCheckByRetwit(o.getRetwits(), loginId));
                        mainDto.setPar_likeCheck(isCheckByLike(o.getParentComment().getLikes(), loginId));
                        mainDto.setPar_retwitCheck(isCheckByRetwit(o.getParentComment().getRetwits(), loginId));
                        mainDto.setLoginMemberPost(checkLoginPost(loginId,o.getMember().getId()));
                        mainDto.setPar_loginMemberPost(checkLoginPost(loginId,o.getParentComment().getMember().getId()));
                        return mainDto;
                    }
                }).collect(Collectors.toList());

        return returnCommentList;
    }

    private List<MainDto> getRetwitDto(Long loginId, List<Retwit> retwitList){
        List<MainDto> returnRetwitList = retwitList.stream()
                .map(o -> {
                    if (o.getPost() != null) {
                        MainDto mainDto = new MainDto(o, true);
                        mainDto.setFUpdateAt(formattingTime(o.getPost().getUpdateAt()));
                        mainDto.setLikeCheck(isCheckByLike(o.getPost().getLikes(), loginId));
                        mainDto.setRetwitCheck(isCheckByRetwit(o.getPost().getRetwits(), loginId));
                        mainDto.setLoginMemberPost(checkLoginPost(loginId,o.getPost().getMember().getId()));
                        return mainDto;
                    } else {
                        MainDto mainDto = new MainDto(o, false);
                        mainDto.setFUpdateAt(formattingTime(o.getPostComment().getUpdateAt()));
                        mainDto.setLikeCheck(isCheckByLike(o.getPostComment().getLikes(), loginId));
                        mainDto.setRetwitCheck(isCheckByRetwit(o.getPostComment().getRetwits(), loginId));
                        mainDto.setLoginMemberPost(checkLoginPost(loginId,o.getPostComment().getMember().getId()));
                        return mainDto;
                    }
                }).collect(Collectors.toList());


        return returnRetwitList;
    }


    /**
     * 비즈니스 로직
     * 같은 리스트 합치기
     * */
    private static List<MainDto> mergeMainList(List<MainDto>... lists){
        return Stream.of(lists)
                .flatMap(List::stream)
                .sorted(Comparator.comparing(MainDto::getUpdateAt).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 비즈니스 로직
     * 자신의 포스트인지 확인하는 로직
     * */
    public static boolean checkLoginPost(Long loginMemberId, Long PostMemberId){
        if(loginMemberId == PostMemberId){
            return true;
        }
        else{
            return false;
        }
    }


    /**
     * 비즈니스 로직
     * 시간 포맷팅
     * */
    // 시간 변경
    public static String formattingTime(LocalDateTime time){

        LocalDateTime now = LocalDateTime.now(); //현재시간
        LocalDateTime oneDayLater = time.plus(1, ChronoUnit.DAYS); // 업로드 시간보다 하루 지난 날짜
        LocalDateTime oneWeekLater = time.plus(1, ChronoUnit.WEEKS); // 업로드 시간보다 일주일 지난 날짜

        String formattedDateTime;

        // 현재시간이 업데이트 날짜보다 하루가 지나기 전일 경우
        if(now.isBefore(oneDayLater)){
            // 포맷 지정
            Duration diff = Duration.between(time, now);
            formattedDateTime = String.format("%d시", diff.toHours());
        }
        // 현재시간이 업데이트 날짜보다 하루 지났을 경우
        else if(now.isBefore(oneWeekLater)){
            // 포맷 지정
            Duration diff = Duration.between(time, now);
            formattedDateTime = String.format("%d일", diff.toDays());
        }
        // 현재시간이 업데이트 날짜보다 일주일이 지났을 경우
        else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY.MM.dd");
            formattedDateTime = time.format(formatter);
        }

        return formattedDateTime;
    }


    /**
     * 비즈니스 로직
     * 좋아요 체크
     * */
    public static boolean isCheckByLike(List<Likes> likeList, Long loginId){
        for(Likes like : likeList){
            if(like.getMember().getId().equals(loginId)){
                return true;
            }
        }
        return false;
    }

    /**
     * 비즈니스 로직
     * 리트윗 체크
     * */

    public static boolean isCheckByRetwit(List<Retwit> retwitList, Long loginId){
        for(Retwit retwit : retwitList){
            if(retwit.getMember().getId().equals(loginId)){
                return true;
            }
        }
        return false;
    }

    /**
     * 비즈니스 로직
     * 이미지 url 체크
     * */
    public static String generateImageUrl(Object obj){
        if (obj instanceof Post){
            if(((Post) obj).getPostImage() != null){
                return  "/uploads/postImage/" + ((Post) obj).getPostImage().getStoreFileName();
            }
            else{
                return null;
            }
        }else if (obj instanceof PostComment){
            if(((PostComment) obj).getPostImage() != null){
                return "/uploads/postImage/" +((PostComment) obj).getPostImage().getStoreFileName();
            }
            else{
                return null;
            }
        }else{
            return null;
        }
    }




    /**
     *
     * 답글 시 이전 Post 찾기
     *
     * */

    public PrevPostDto getPrevPost(Long prevId, Boolean check){
        if(check){
            Post post = postRepository.findPost(prevId);
            return new PrevPostDto(post.getId(),post.getMember().getNickName(), post.getMember().getPersonal(), post.getMember().getProfileImg(), post.getContent(), post);
        }
        else{
            PostComment comment = commentRepository.findComment(prevId);
            return new PrevPostDto(comment.getId(), comment.getMember().getNickName(), comment.getMember().getPersonal(), comment.getMember().getProfileImg(), comment.getCommentText(), comment);
        }
    }




}
