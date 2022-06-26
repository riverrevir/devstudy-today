package today.devstudy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import today.devstudy.config.jwt.JwtRequestFilter;
import today.devstudy.config.jwt.JwtTokenUtil;
import today.devstudy.dto.studyTask.*;
import today.devstudy.service.StudyTaskService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/study")
@RestController
public class StudyTaskController {
    private final StudyTaskService studyTaskService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/start")
    public List<StartStudyTaskResponse> startStudyTask(@RequestBody StartStudyTaskRequest startStudyTaskRequest, @CookieValue(value = "bearer") String token) {
        String userId = jwtTokenUtil.getUsernameFromToken(token);
        return studyTaskService.startStudyTask(startStudyTaskRequest, userId);
    }

    @PostMapping("/end")
    public List<EndStudyTaskResponse> endStudyTask(@RequestBody EndStudyTaskRequest endStudyTaskRequest, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(JwtRequestFilter.HEADER_KEY);
        String userId = jwtTokenUtil.getUsernameFromToken(token);
        return studyTaskService.endStudyTask(endStudyTaskRequest, userId);
    }
    @GetMapping("/day")
    public List<FindStudyTaskResponse> findDayStudyTask(@RequestParam int year, @RequestParam int month, @RequestParam int day, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader(JwtRequestFilter.HEADER_KEY);
        String userId = jwtTokenUtil.getUsernameFromToken(token);
        return studyTaskService.findStudyForDay(year,month,day,userId);
    }

    @GetMapping("/month")
    public List<FindStudyTaskResponse> findMonthStudyTask(@RequestParam int year,@RequestParam int month, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader(JwtRequestFilter.HEADER_KEY);
        String userId = jwtTokenUtil.getUsernameFromToken(token);
        return studyTaskService.findStudyForMonth(year, month, userId);
    }

    @GetMapping("/year")
    public List<FindStudyTaskResponse> findSixMonthStudyTask(@RequestParam int year, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader(JwtRequestFilter.HEADER_KEY);
        String userId = jwtTokenUtil.getUsernameFromToken(token);
        return studyTaskService.findStudyForYear(year, userId);
    }
}
