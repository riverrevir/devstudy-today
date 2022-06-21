package today.devstudy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import today.devstudy.dto.studyTask.*;
import today.devstudy.service.StudyTaskService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/study-task")
@RestController
public class StudyTaskController {
    private final StudyTaskService studyTaskService;

    @PostMapping("/start")
    public List<StartStudyTaskResponse> startStudyTask(@RequestBody StartStudyTaskRequest startStudyTaskRequest){
        String userId = "";
        return studyTaskService.startStudyTask(startStudyTaskRequest,userId);
    }

    @PostMapping("/end")
    public List<EndStudyTaskResponse> endStudyTask(@RequestBody EndStudyTaskRequest endStudyTaskRequest){
        String userId = "";
        return studyTaskService.endStudyTask(endStudyTaskRequest,userId);
    }

    @GetMapping("/{studyTaskNumber}")
    public FindStudyTaskResponse findStudyTask(@PathVariable Long studyTaskNumber){
        return studyTaskService.findStudyTask(studyTaskNumber);
    }
}
