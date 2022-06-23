package today.devstudy.service;

import groovy.util.logging.Log;
import groovy.util.logging.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import today.devstudy.config.email.EmailUtil;
import today.devstudy.config.jwt.JwtTokenUtil;
import today.devstudy.domain.User;
import today.devstudy.dto.user.*;
import today.devstudy.exception.InputNotFoundException;
import today.devstudy.repository.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailUtil emailUtil;
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

    public UserCreateAuthResponse checkUserIdDuplication(String userid) {
        boolean userIdDuplication = userRepository.existsByUserId(userid);
        return new UserCreateAuthResponse(userIdDuplication);
    }

    public UserCreateAuthResponse checkEmailDuplication(String email) {
        boolean emailDuplication = userRepository.existsByEmail(email);
        return new UserCreateAuthResponse(emailDuplication);
    }

    public EmailResponse createMailAndChangePassword(EmailRequest request) {
        String email = request.getEmail();
        String password = emailUtil.getTempPassword();
        Email emailDto = emailUtil.createMail(email, password);
        emailUtil.sendMail(emailDto);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("잘못된 이메일 요청입니다."));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return new EmailResponse("success");
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
