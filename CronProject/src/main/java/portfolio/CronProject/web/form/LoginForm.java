package portfolio.CronProject.web.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginForm {

    @NotEmpty(message = "아이디를 입력해주세요")
    private String name;

    @NotEmpty(message = "비밀번호를 입력해주세요")
    private String password;
}
