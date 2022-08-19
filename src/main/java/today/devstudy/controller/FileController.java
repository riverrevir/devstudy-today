package today.devstudy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import today.devstudy.config.jwt.JwtRequestFilter;
import today.devstudy.config.jwt.JwtTokenUtil;
import today.devstudy.service.FileService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api")
public class FileController {
    private final FileService fileService;
    private final JwtTokenUtil jwtTokenUtil;
    @GetMapping("/img")
    public void downloadImage(@RequestHeader(value = JwtRequestFilter.HEADER_KEY) String token, HttpServletResponse httpServletResponse){
        String jwtToken = jwtTokenUtil.splitToken(token);
        String userId = jwtTokenUtil.getUsernameFromToken(jwtToken);
        fileService.downloadImg(userId,httpServletResponse);
    }

    @PostMapping("/img")
    public void uploadImage(@RequestHeader(value = JwtRequestFilter.HEADER_KEY) String token, @RequestPart MultipartFile file, HttpServletResponse httpServletResponse) throws IOException {
        String jwtToken = jwtTokenUtil.splitToken(token);
        String userId = jwtTokenUtil.getUsernameFromToken(jwtToken);
        fileService.uploadImg(userId,file,httpServletResponse);
    }
}
