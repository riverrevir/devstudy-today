package today.devstudy.service;


import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import today.devstudy.domain.User;

@SpringBootTest
@Transactional
public class RegisterTest {
    private Logger log = (Logger) LoggerFactory.getLogger(RegisterTest.class);
    @Autowired
    private UserService userService;

    @Test
    public void register() {
        User user = userService.create("id", "email@co.kr", "1234", "male");
    }
}
