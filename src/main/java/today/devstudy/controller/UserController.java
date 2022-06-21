package today.devstudy.controller;

import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import today.devstudy.config.ApiResponse;
import today.devstudy.dto.user.LoginRequest;
import today.devstudy.dto.user.LoginResponse;
import today.devstudy.service.UserService;
import today.devstudy.dto.user.UserCreateForm;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/register")
    public String register(Model model, UserCreateForm userCreateForm) {
        model.addAttribute("sex");
        return "register_form";
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<ApiResponse> register(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        try {
            userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(), userCreateForm.getPassword1(), userCreateForm.getSex());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("registerFailed", "이미 등록된 사용자입니다.");
            return new ResponseEntity<>(new ApiResponse(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("registerFailed", e.getMessage());
            return new ResponseEntity<>(new ApiResponse(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ApiResponse(), HttpStatus.OK);
    }


    @GetMapping("/login")
    public String login() {

        return "login_form";
    }

    @PostMapping("/login")
    @ResponseBody
    public LoginResponse loginResponse(@RequestBody LoginRequest loginRequest) throws Exception {
        return userService.login(loginRequest);
    }
}
