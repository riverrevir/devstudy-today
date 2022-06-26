package today.devstudy.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import today.devstudy.domain.user.UserRepository;

@SpringBootTest
public class LoginTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Test
    public void loginApi(){
        String id = "aaa";
        String password = "123123123";
    }

}
