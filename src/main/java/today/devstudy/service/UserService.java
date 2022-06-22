package today.devstudy.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import today.devstudy.config.jwt.JwtTokenUtil;
import today.devstudy.domain.User;
import today.devstudy.dto.user.LoginRequest;
import today.devstudy.dto.user.LoginResponse;
import today.devstudy.dto.user.UserCreateRequest;
import today.devstudy.dto.user.UserCreateResponse;
import today.devstudy.exception.InputNotFoundException;
import today.devstudy.repository.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtTokenUtil jwtTokenUtil;

    public UserCreateResponse create(UserCreateRequest request) {
        final String userId = request.getUserId();
        final String password = request.getPassword1();
        final String sex = request.getSex();
        final String email = request.getEmail();
        User user = new User();
        user.setUserId(userId);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setSex(sex);
        userRepository.save(user);
        String token = jwtTokenUtil.generateToken(userId);
        return new UserCreateResponse(token);
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
