package today.devstudy.controller;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<UserCreateResponse> register(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        HttpHeaders headers=new HttpHeaders();
        headers.set("Authorization","Bearer "+userService.create(userCreateRequest).getToken());
        return new ResponseEntity<>(headers,HttpStatus.OK);
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
    public ResponseEntity<LoginResponse> loginResponse(@RequestBody LoginRequest loginRequest) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userService.login(loginRequest).getToken());
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PostMapping("/change/password")
    public ChangePasswordResponse changePassword(@RequestHeader(value = "Authorization") String token, @Valid @RequestBody ChangePasswordRequest request) {
        return userService.findByUserIdAndPasswordChange(token, request);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logoutResponse(@RequestHeader(value = "Authorization") String token) {
        token = "";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}
