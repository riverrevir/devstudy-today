package today.devstudy.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import today.devstudy.domain.User;
import today.devstudy.repository.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User create(String userId, String email, String password, String sex) {
        User user = new User();
        user.setUserId(userId);
        user.setEmail(email);
        user.setSex(sex);
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }

    public User login(String username, String password) throws Exception {
        User user = this.userRepository.findByUserId(username).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new Exception("비밀번호가 틀립니다.");
        }
        return user;
    }

    public Optional<User> findUserByUserId(String UserId) {
        Optional<User> optionalUser = userRepository.findByUserId(UserId);
        return optionalUser;
    }
}
