package today.devstudy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import today.devstudy.config.ApiResponse;
import today.devstudy.config.jwt.JwtRequest;
import today.devstudy.config.jwt.JwtTokenUtil;
import today.devstudy.service.JwtUserDetailService;

@RestController
@CrossOrigin
public class JwtAuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailService jwtUserDetailService;

    @RequestMapping(value = "/api/user/auth", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUserid(), authenticationRequest.getPassword());
        final UserDetails userDetails = jwtUserDetailService.loadUserByUsername(authenticationRequest.getUserid());
        final String token = jwtTokenUtil.generateToken(userDetails);
        System.out.println(token);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(token);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    private void authenticate(String userid, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userid, password));
        } catch (DisabledException ex) {
            throw new DisabledException("USER_DISABLED", ex);
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
        }
    }
}
