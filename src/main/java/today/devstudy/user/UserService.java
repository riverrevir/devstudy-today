package today.devstudy.user;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import today.devstudy.email.Email;
import today.devstudy.email.EmailConfirmationTokenService;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public User create(String username, String email, String password,String sex){

            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setSex(sex);
            user.setPassword(passwordEncoder.encode(password));
            this.userRepository.save(user);
            return user;
    }


}
