package portfolio.CronProject.web.dto;

import lombok.Data;
import portfolio.CronProject.domain.Member;

@Data
public class FilterDto {
    private String personal;
    private String profileImg;
    private boolean select;

    public FilterDto(Member member, Long findId) {
        personal = member.getPersonal();
        profileImg = member.getProfileImg();
        select = checkSelect(member.getId(), findId);
    }

    private boolean checkSelect(Long memberId, Long findId){
        if(memberId == findId){
            return true;
        }
        else{
            return false;
        }
    }
}
