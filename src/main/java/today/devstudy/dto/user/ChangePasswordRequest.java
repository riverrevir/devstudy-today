package today.devstudy.dto.user;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class ChangePasswordRequest {
    private String currentPassword;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password2;
}
