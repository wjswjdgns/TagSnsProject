package portfolio.CronProject.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.CronProject.domain.Follow;
import portfolio.CronProject.domain.Member;
import portfolio.CronProject.domain.UserTag;
import portfolio.CronProject.repository.MemberRepository;
import portfolio.CronProject.repository.TagRepository;
import portfolio.CronProject.web.dto.FollowTagsDto;
import portfolio.CronProject.web.dto.NotificationDto;
import portfolio.CronProject.web.dto.SearchTagDto;
import portfolio.CronProject.web.dto.TagsDto;
import portfolio.CronProject.web.form.UpdateForm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final MemberRepository memberRepository;

    // 태그 삭제하기
    public void removeTag(Long loginId, List<UpdateForm.createTag> newUserTags){


        Member member = memberRepository.findOne(loginId);
        List<UserTag> userTagList = member.getUserTag();

        List<UserTag> remainUserTags = userTagList.stream()
                .filter(existingUserTag -> newUserTags.stream()
                        .noneMatch(newUserTag -> newUserTag.getTagName().equals(existingUserTag.getTagName())))
                .collect(Collectors.toList());

        if(!remainUserTags.isEmpty()){
            tagRepository.deleteTag(remainUserTags);
        }


    }
    // 태그 저장하기
    public void createExistTag(Long loginId, List<UpdateForm.createTag> newUserTags){

        Member member = memberRepository.findOne(loginId);
        List<UserTag> userTagList = member.getUserTag(); // 유저가 현재 가지고 있는 태그 리스트

        // 이름이 중복되는 것을 제외해서 새롭게 리스트를 구성한다.
        List<UpdateForm.createTag> distinctUserTags = newUserTags.stream()
                .filter(newUserTag -> userTagList.stream()
                        .noneMatch(existingUserTag -> existingUserTag.getTagName().equals(newUserTag.getTagName())))
                .collect(Collectors.toList());

        for(UpdateForm.createTag createTag : distinctUserTags){
            UserTag userTag = new UserTag();
            userTag.setMember(member);
            userTag.setTagName(createTag.getTagName());
            userTag.setTagCount(createTag.getTagCount());
            userTag.setWeekCount(createTag.getWeekCount());
            tagRepository.createTag(userTag);
        }

    }

    // 태그 신규 저장하기
    public void createTag(Long loginId, List<UpdateForm.createTag> newUserTags){

        Member member = memberRepository.findOne(loginId);
        for(UpdateForm.createTag createTag :newUserTags){
            UserTag userTag = new UserTag();
            userTag.setMember(member);
            userTag.setTagName(createTag.getTagName());
            userTag.setTagCount(createTag.getTagCount());
            userTag.setWeekCount(createTag.getWeekCount());
            tagRepository.createTag(userTag);
        }
    }

    // 태그 불러오기
    public List<UserTag> getTagList(Long memberId){
        return tagRepository.findUserTag(memberId);
    }

    // 팔로우들의 태그 불러오기
    public List<FollowTagsDto> getFollowTagList(Long loginId){

        Member member = memberRepository.findOne(loginId);
        List<Follow> followList = member.getFollow();

        List<FollowTagsDto> followTagsDtos = new ArrayList<>();

        // follow 리스트의 멤버들을 받아야 한다.
        for(Follow follow : followList){
            Member followMember = memberRepository.findOne(follow.getFollowMemberId());

            if(!tagRepository.findFollowUserTag(followMember).isEmpty()){
                List<UserTag> followUserTag = tagRepository.findFollowUserTag(followMember);

                FollowTagsDto followTagsDto = new FollowTagsDto(followMember.getNickName(), followMember.getPersonal(), followMember.getProfileImg());

                List<TagsDto> tagsdto = followUserTag.stream()
                        .map(o -> new TagsDto(o))
                        .collect(Collectors.toList());

                followTagsDto.setTagList(tagsdto);

                followTagsDtos.add(followTagsDto);
            }
        }

        // 자신의 태그 리스트를 만들어서 합친다.
        if(!tagRepository.findFollowUserTag(member).isEmpty()) {
            List<UserTag> followUserTag = tagRepository.findFollowUserTag(member);
            FollowTagsDto MyTagListDto = new FollowTagsDto(member.getNickName(), member.getPersonal(), member.getProfileImg());

            List<TagsDto> MyTagList = followUserTag.stream()
                    .map(o -> new TagsDto(o))
                    .collect(Collectors.toList());

            MyTagListDto.setTagList(MyTagList);
            followTagsDtos.add(MyTagListDto);
        }

        return followTagsDtos;

    }

    // 팔로우 태그 중 인기 확인
    public List<SearchTagDto> getLoginRecommended(Long loginId){
        List<Member> followingMember = memberRepository.findFollowingMember(loginId);
        List<UserTag> loginRecommended = tagRepository.findLoginRecommended(followingMember);
        return loginRecommended.stream()
                .map(o -> new SearchTagDto(o))
                .collect(Collectors.toList());
    }

    // 전체 태그 중 인기 확인
    public List<SearchTagDto> getRecommended(){
        List<UserTag> recommended = tagRepository.findRecommended();
        return recommended.stream()
                .map(o -> new SearchTagDto(o))
                .collect(Collectors.toList());
    }

}
