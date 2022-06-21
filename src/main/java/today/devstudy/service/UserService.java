package today.devstudy.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import today.devstudy.config.jwt.JwtTokenUtil;
import today.devstudy.domain.User;
import today.devstudy.dto.user.LoginRequest;
import today.devstudy.dto.user.LoginResponse;
import today.devstudy.exception.InputNotFoundException;
import today.devstudy.repository.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtTokenUtil jwtTokenUtil;

    public User create(String userId, String email, String password, String sex) {
        User user = new User();
        user.setUserId(userId);
        user.setEmail(email);
        user.setSex(sex);
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }

    public LoginResponse login(LoginRequest request) throws Exception {
        final String userId = request.getUserId();
        final String password = request.getPassword();
        User user = this.userRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호를 확인해주세요."));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호를 확인해주세요.");
        }
        String token = jwtTokenUtil.generateToken(userId);
        return new LoginResponse(token);
    }
}
