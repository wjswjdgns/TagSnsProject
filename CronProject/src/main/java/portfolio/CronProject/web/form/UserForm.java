package portfolio.CronProject.web.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Data
public class UserForm {
    @NotEmpty(message = "아이디는 필수 입력입니다.")
    private String name;

    @NotEmpty(message = "패스워드는 필수 입력입니다.")
    private String password;
    @NotEmpty(message = "닉네임은 필수 입력입니다.")
    private String nickname;

    @NotEmpty(message = "고유아이디는 필수 입력입니다.")
    private String personal;

}
