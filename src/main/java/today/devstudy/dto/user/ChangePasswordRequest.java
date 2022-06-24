package today.devstudy.dto.user;

import lombok.Getter;

@Getter
public class ChangePasswordRequest {
    private String currentPassword;
    private String password1;
    private String password2;
}
