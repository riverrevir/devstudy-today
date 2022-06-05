package today.devstudy.user;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;

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

    public User login(String username,String password) throws Exception {
        User user = userRepository.findByusername(username);
        if(user==null) {
            System.out.println("1번오류");
            throw new Exception("사용자를 찾을 수 없습니다.");
        }
        System.out.println(password);
        if(!passwordEncoder.matches(password,user.getPassword())){
            System.out.println("2번오류");
            throw new Exception("비밀번호가 틀립니다.");
        }
        return user;
    }

}
