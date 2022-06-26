package today.devstudy.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import today.devstudy.config.jwt.JwtRequestFilter;
import today.devstudy.config.jwt.JwtTokenUtil;
import today.devstudy.dto.studyTask.*;
import today.devstudy.service.StudyTaskService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Api(tags={"2. StudyTask"})
@RequiredArgsConstructor
@RequestMapping("/api/study")
@RestController
public class StudyTaskController {
    private final StudyTaskService studyTaskService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/start")
    public List<StartStudyTaskResponse> startStudyTask(@RequestBody StartStudyTaskRequest startStudyTaskRequest, @RequestHeader(value = JwtRequestFilter.HEADER_KEY) String token) {
        String jwtToken = jwtTokenUtil.splitToken(token);
        String userId = jwtTokenUtil.getUsernameFromToken(jwtToken);
        return studyTaskService.startStudyTask(startStudyTaskRequest, userId);
    }

    @PostMapping("/end")
    public List<EndStudyTaskResponse> endStudyTask(@RequestBody EndStudyTaskRequest endStudyTaskRequest, @RequestHeader(value = JwtRequestFilter.HEADER_KEY) String token) {
        String jwtToken = jwtTokenUtil.splitToken(token);
        String userId = jwtTokenUtil.getUsernameFromToken(jwtToken);
        return studyTaskService.endStudyTask(endStudyTaskRequest, userId);
    }

    @GetMapping("/day/ongoing")
    public CountStudyTaskForDayResponse countOnGoingDayStudyTask(@RequestParam int year, @RequestParam int month, @RequestParam int day, @RequestHeader(value = JwtRequestFilter.HEADER_KEY) String token) {
        String jwtToken = jwtTokenUtil.splitToken(token);
        String userId = jwtTokenUtil.getUsernameFromToken(jwtToken);
        return studyTaskService.countOnGoingStudyForDay(year, month, day, userId);
    }

    @GetMapping("/day/completed")
    public CountStudyTaskForDayResponse countCompletedDayStudyTask(@RequestParam int year, @RequestParam int month, @RequestParam int day, @RequestHeader(value = JwtRequestFilter.HEADER_KEY) String token) {
        String jwtToken = jwtTokenUtil.splitToken(token);
        String userId = jwtTokenUtil.getUsernameFromToken(jwtToken);
        return studyTaskService.countCompletedStudyForDay(year, month, day, userId);
    }

    @GetMapping("/month")
    public List<FindStudyTaskResponse> findMonthStudyTask(@RequestParam int year, @RequestParam int month, @RequestHeader(value = JwtRequestFilter.HEADER_KEY) String token) {
        String jwtToken = jwtTokenUtil.splitToken(token);
        String userId = jwtTokenUtil.getUsernameFromToken(jwtToken);
        return studyTaskService.findStudyForMonth(year, month, userId);
    }

    @GetMapping("/year")
    public List<FindStudyTaskResponse> findSixMonthStudyTask(@RequestParam int year, @RequestHeader(value = JwtRequestFilter.HEADER_KEY) String token) {
        String jwtToken = jwtTokenUtil.splitToken(token);
        String userId = jwtTokenUtil.getUsernameFromToken(jwtToken);
        return studyTaskService.findStudyForYear(year, userId);
    }
}
