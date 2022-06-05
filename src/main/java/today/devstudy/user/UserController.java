package today.devstudy.user;

import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    List<String> gender;

    @ModelAttribute
    public void genderLoad() {
        gender = new ArrayList<String>();
        gender.add("남");
        gender.add("여");
    }

    /**
     * register(회원가입)
     *
     * @param model
     * @param userCreateForm
     * @return
     */
    @GetMapping("/register")
    public String register(Model model, UserCreateForm userCreateForm) {
        model.addAttribute("sex", gender);
        return "register_form";
    }

    @PostMapping("/register")
    public String register(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register_form";
        }
        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "패스워드가 일치하지 않습니다.");
            return "register_form";
        }
        try {
            userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(), userCreateForm.getPassword1(), userCreateForm.getSex());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("registerFailed", "이미 등록된 사용자입니다.");
            return "register_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("registerFailed", e.getMessage());
            return "register_form";
        }
        return "redirect:/";
    }


    @GetMapping("/login")
    public String login() {

        return "login_form";
    }

    @PostMapping("/login")
    public String login(String username, String password) {
        try {
            userService.login(username, password);
        } catch (Exception e) {
            System.out.println("3번오류");
            return "login_form";
        }
        return "redirect:/";
    }

}
