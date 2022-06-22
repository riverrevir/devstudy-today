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
import today.devstudy.dto.user.UserCreateResponse;
import today.devstudy.service.UserService;
import today.devstudy.dto.user.UserCreateRequest;

@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public UserCreateResponse register(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        return userService.create(userCreateRequest);
    }

    @PostMapping("/login")
    @ResponseBody
    public LoginResponse loginResponse(@RequestBody LoginRequest loginRequest) throws Exception {
        return userService.login(loginRequest);
    }
}
