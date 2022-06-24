package today.devstudy.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import today.devstudy.dto.user.*;
import today.devstudy.service.UserService;

@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public UserCreateResponse register(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        return userService.create(userCreateRequest);
    }

    @GetMapping("/auth/id/{userId}/exists")
    public UserCreateAuthResponse checkUserIdDuplicate(@PathVariable(value = "userId") String userid) {
        return userService.checkUserIdDuplication(userid);
    }

    @GetMapping("/auth/email/{email}/exists")
    public UserCreateAuthResponse checkEmailDuplicate(@PathVariable(value = "email") String email) {
        return userService.checkEmailDuplication(email);
    }

    @PostMapping("/find/password")
    public EmailResponse findPassword(@RequestBody EmailRequest emailRequest) {
        return userService.createMailAndChangePassword(emailRequest);
    }

    @GetMapping("/find/userid")
    public FindUserIdResponse findByUserId(@RequestBody FindUserIdRequest findUserIdRequest) {
        return userService.emailCheckAndFindByUserId(findUserIdRequest);
    }

    @PostMapping("/login")
    @ResponseBody
    public LoginResponse loginResponse(@RequestBody LoginRequest loginRequest) throws Exception {
        return userService.login(loginRequest);
    }

    @PostMapping("/change/password")
    public ChangePasswordResponse changePassword(@RequestHeader(value = "Authorization") String token, @RequestBody ChangePasswordRequest request) {
        System.out.println(token);
        return userService.findByUserIdAndPasswordChange(token, request);
    }
}
