package today.devstudy.dto.user;

import lombok.*;
import today.devstudy.domain.user.User;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class FindUserInfoResponse {
    private Long number;
    private String userId;
    private String sex;
    private String email;

    public static FindUserInfoResponse from(User user){
        Long number = user.getNumber();
        String userId = user.getUserId();
        String sex = user.getSex();
        String email = user.getEmail();

        return new FindUserInfoResponse(number,userId,sex,email);
    }
}
