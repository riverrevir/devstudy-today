package today.devstudy.config.email;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import today.devstudy.dto.user.Email;

@Component
@RequiredArgsConstructor
public class EmailUtil {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;

    public Email createMail(String email, String password) {
        Email emailDto = new Email();
        emailDto.setAddress(email);
        emailDto.setTitle("[DevStudy] 임시 비밀번호 안내 이메일 입니다.");
        emailDto.setMessage("새로운 임시 비밀번호 생성 안내를 위해 발송된 메일입니다." + "임시 비밀번호는" + password + "입니다." + "임시 비밀번호를 입력하시고 비밀 번호를 변경해주시기 바랍니다.");
        return emailDto;
    }

    public String getTempPassword() {
        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        String password = "";
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            password += charSet[idx];
        }
        return password;
    }

    public void sendMail(Email email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email.getAddress());
        message.setSubject(email.getTitle());
        message.setText(email.getMessage());
        message.setFrom(from);
        message.setReplyTo(from);
        javaMailSender.send(message);
    }

}
