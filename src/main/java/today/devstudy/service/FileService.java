package today.devstudy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import today.devstudy.domain.user.User;
import today.devstudy.domain.user.UserRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private static final String root = "/home/devstudy/img/";
    private static final String baseProfile = "base-profile";
    private final UserRepository userRepository;

    public void downloadImg(String userId, HttpServletResponse response) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));
        String imgName = user.getProfile();
        String imgSrc = new StringBuilder(root).append("/").append(imgName).append(".").append(user.getType()).toString();
        try (FileInputStream fis = new FileInputStream(imgSrc);
             OutputStream out = response.getOutputStream();
        ) {
            int readCount = 0;
            byte[] buffer = new byte[1024];
            while ((readCount = fis.read(buffer)) != -1) {
                out.write(buffer, 0, readCount);
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("", e);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void uploadImg(String userId, MultipartFile file, HttpServletResponse response) throws IOException {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));
        String contentType = file.getContentType();
        String fileExtension;
        deleteImg(user.getProfile(), user.getType());
        if (ObjectUtils.isEmpty(contentType)) { //기본 프로필로 변경요청
            user.setProfile(baseProfile);
            user.setType("jpg");
        } else { // 새로운 프로필로 변경요청
            if (contentType.contains("image/jpeg")) {
                fileExtension = "jpg";
            } else if (contentType.contains("image/png")) {
                fileExtension = "png";
            } else {
                response.sendError(404, "올바르지 않은 파일입니다.");
                return;
            }
            UUID uuid = UUID.randomUUID();
            String uploadFileName = uuid.toString() + "_" + userId;
            File saveFile = new File(root + File.separator + uploadFileName + "." + fileExtension);
            file.transferTo(saveFile);
            user.setProfile(uploadFileName);
            user.setType(fileExtension);
        }
        userRepository.save(user);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void deleteImg(String userProfile, String fileExtension) {
        if (!userProfile.equals(baseProfile)) {
            File deleteFile = new File(root + File.separator + userProfile + "." + fileExtension);
            if (deleteFile.exists()) {
                deleteFile.delete();
            }
        }
    }
}
