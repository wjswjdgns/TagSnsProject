package portfolio.CronProject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.CronProject.domain.*;
import portfolio.CronProject.repository.*;
import portfolio.CronProject.web.dto.NotificationDto;
import portfolio.CronProject.web.dto.NotificationSettingDto;
import portfolio.CronProject.web.dto.tagListDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotificationsService {
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final TagRepository tagRepository;


    // 사용자 알림 취소하기
    public void removeNotification(Long loginId, Long findId){

        // 알림 받기
        NoticeMember loginNotification = notificationRepository.findNoticeMember(loginId, findId);

        // 이미 등록한 것이 있을 경우 삭제
        if(loginNotification != null){
            notificationRepository.cancel(loginId, findId);;
        }

    }

    // 사용자 알림 & 태그 알림 조회하기
    public NotificationSettingDto getNotificationSetting(Long loginId, Long findId){
        NoticeMember loginNotification = notificationRepository.findNoticeMember(loginId, findId);

        Member findMember = memberRepository.findOne(findId);
        Member member = memberRepository.findOne(loginId);

        // userTagList를 tagListDto로 변환
        List<UserTag> userTagList = tagRepository.findUserTag(findId);
        List<UserTag> selectTagList = tagRepository.findSelectTagList(loginId, findId);

        List<tagListDto> tagListDtos = userTagList.stream()
                .map(o -> new tagListDto(o))
                .collect(Collectors.toList());

        if(selectTagList != null){
            for (tagListDto tagListDto :tagListDtos) {
                for(UserTag selectTag : selectTagList){
                    if(tagListDto.getTagName().equals(selectTag.getTagName())){
                        tagListDto.setCheck(true);
                    }
                }
            }
        }

        NotificationSettingDto notificationSettingDto;

        if(loginNotification == null){
            notificationSettingDto = new NotificationSettingDto(findMember.getPersonal(), 0,tagListDtos);
        }
        else{
            notificationSettingDto = new NotificationSettingDto(findMember.getPersonal(), loginNotification.getNoticeSet(),tagListDtos);
        }

        // 로그인 회원이 특정 태그를 활성화 했는지 등록하기
        // tag


        return notificationSettingDto;
    }

    
    // 알림 설정하기 (글쓰기 및 댓글 올라왔을 때 전달되는 알림)
    public void saveNotificationMember(Long loginId, Long findId, Integer status){

        // 알림을 설정 했는지 안했는지를 확인
        NoticeMember loginNotification = notificationRepository.findNoticeMember(loginId, findId);

        // 변경 했을 경우
        if(loginNotification != null){
            // 기존의 있던 값이 있고 세팅이 같은 경우 (중복 체크)
            if(loginNotification.getNoticeSet() == status){
                return;
            }
            loginNotification.setNoticeSet(status);
        }
        // 새롭게 만드는 경우
        else{
            Member member = memberRepository.findOne(findId);
            NoticeMember noticeMember = new NoticeMember();
            noticeMember.setMember(member);
            noticeMember.setActiveMemberId(loginId);
            noticeMember.setNoticeSet(status);
            notificationRepository.saveMember(noticeMember);
        }
    }


    // 태그 알림 설정하기 (특정 태그에 글이 올라 왔을 경우 전달되는 알림)
    public void saveNotificationTag(Long loginId, List<Long> tagsId, List<Long> removeIdList){

        Member member = memberRepository.findOne(loginId);
        // 반복해서 태그를 찾은 다음 리스트를 만든다.
        for (Long tagId:tagsId) {
            // 확인 여부
            NoticeTag ChecknoticeTag = notificationRepository.findNotificationTags(loginId, tagId);

            // 이미 존재하고 있다면
            if(ChecknoticeTag != null){
                continue;
            }
            else{
                NoticeTag noticeTag = new NoticeTag();
                noticeTag.setMember(member);
                noticeTag.setUserTag(tagRepository.findTag(tagId));
                notificationRepository.saveTag(noticeTag);
            }
        }

        for(Long tagId : removeIdList){
            NoticeTag ChecknoticeTag = notificationRepository.findNotificationTags(loginId, tagId);

            // 원래 없다면 넘어가라
            if(ChecknoticeTag == null){
                continue;
            }
            // 존재 한다면 삭제해라
            else{
                notificationRepository.cancelTag(loginId, tagId);
            }
        }

    }

    

    // 알림 등록하기 (글쓰기와 댓글)
    // status == 1 (글쓰기),  status == 2 (댓글)
    public void savePostNotification(Long loginId, Long postId, boolean check, Integer status){
        Member member = memberRepository.findOne(loginId);
        List<NoticeMember> noticeMembers = member.getNoticeMembers();// 여기서 자신을 알림 설정 한 사람을 확인한다.

        Post post = null;
        PostComment comment = null;
        if(check){
            post = postRepository.findPost(postId);
        }
        else{
            comment = commentRepository.findComment(postId);
        }

        // 알림 설정을 했을 경우 여기서 등록이 된다.
        // 알림 설정한 사람들을 반복하면서 알림 설정을 해준다.
        for (NoticeMember noticeMember : noticeMembers){
            Member findMember = memberRepository.findOne(noticeMember.getActiveMemberId());
            // 모든 글쓰기 알림 상태에서 댓글이 올라왔을 경우에는 알리지 않는다.
            if(noticeMember.getNoticeSet() == 1 && status == 2){
                continue;
            }
            // 나머지 상황에서는 모두 알려준다. 
            else{
                // Notice 추가
                Notice notice = new Notice();
                notice.setMember(findMember);
                notice.setActivePersonal(member.getPersonal());
                notice.setActiveProfileImg(member.getProfileImg());
                notice.setNoticeStatus(1); // 글쓰기 알림
                if(check){
                    notice.setPost(post);
                }
                else{
                    notice.setPostComment(comment);
                }
                notice.setCreateAt(LocalDateTime.now());
                // 저장
                notificationRepository.saveNotification(notice);
            }

        }
    }

    // Member의 알림 설정 상태와 상관없이 알림 진행 (주체가 글을 남긴 사람에게 알림)
    // 좋아요 2 리트윗 3 댓글 4
    public void saveReactionNotification(Long loginId, Long postId, boolean check, Integer status){
        Member loginMember = memberRepository.findOne(loginId);

        Member postMember;
        Post post = null;
        PostComment postComment = null;

        if(check){
            post = postRepository.findPost(postId);
            postMember = post.getMember();
        }
        else{
            postComment = commentRepository.findComment(postId);
            postMember = postComment.getMember();
        }

        // 자신의 글에 좋아요, 리트윗, 댓글을 남겼을 경우 아무런 행동을 하지 않는다.
        if(loginMember.equals(postMember)){
            return;
        }

        Notice notice = new Notice();
        notice.setMember(postMember); // 알림 받는 주체는 포스트를 쓴 사람
        notice.setActivePersonal(loginMember.getPersonal());
        notice.setActiveProfileImg(loginMember.getProfileImg());
        notice.setNoticeStatus(status); // 좋아요 알림
        if(check){
            notice.setPost(post);
        }
        else{
            notice.setPostComment(postComment);
        }
        notice.setCreateAt(LocalDateTime.now());
        // 저장
        notificationRepository.saveNotification(notice);
    }

    // 태그 알림 등록
    public void saveTagNotification(Long postId, Long tagId) {

        // 태그 알림을 설정한 사람들을 찾는다.
        UserTag tag = tagRepository.findTag(tagId);
        Member member = tag.getMember();

        Post post = postRepository.findPost(postId);
        List<NoticeTag> noticeTagList = tag.getNoticeTag(); // 새로 알림 설정한 사람들

        for (NoticeTag noticeTag :noticeTagList) {
            Member findMember = noticeTag.getMember();
            Notice notice = new Notice();
            notice.setMember(findMember);
            notice.setActivePersonal(member.getPersonal());
            notice.setActiveProfileImg(member.getProfileImg());
            notice.setNoticeStatus(4); // 태그
            notice.setPost(post);
            notice.setUserTag(tag);
            notice.setCreateAt(LocalDateTime.now());
            // 저장
            notificationRepository.saveNotification(notice);
        }

    }


    // 멤버 알림 가져오기
    public List<NotificationDto> getNotification(Long loginId){
        List<Notice> notification = notificationRepository.findNotification(loginId);

        List<NotificationDto> result = notification.stream()
                .filter(o -> o.getNoticeStatus() != 4)
                .map(o -> new NotificationDto(o))
                .collect(Collectors.toList());
        return result;
    }

    public List<NotificationDto> getTagNotification(Long loginId){
        List<Notice> notification = notificationRepository.findNotification(loginId);

        List<NotificationDto> result = notification.stream()
                .filter(o -> o.getNoticeStatus() == 4)
                .map(o -> new NotificationDto(o))
                .collect(Collectors.toList());
        return result;
    }

    /**
     * 비즈니스 로직
     * Dto 만들기
     * */


}
