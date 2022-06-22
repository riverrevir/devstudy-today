package today.devstudy.service;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.digester.ArrayStack;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.devstudy.domain.studyTask.StudyTask;
import today.devstudy.domain.User;
import today.devstudy.dto.studyTask.*;
import today.devstudy.domain.studyTask.StudyTaskRepository;
import today.devstudy.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class StudyTaskService {
    private final StudyTaskRepository studyTaskRepository;
    private final UserRepository userRepository;

    public List<StartStudyTaskResponse> startStudyTask(StartStudyTaskRequest startStudyTaskRequest, String userId) {
        List<StudyTask> studyTasks = StartStudyTaskRequest.newStudyTasks(startStudyTaskRequest);
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        studyTasks.forEach(studyTask -> {
            studyTask.startStudyTask(LocalDateTime.now(), user);
            studyTaskRepository.save(studyTask);
        });

        return studyTasks.stream()
                .map(StartStudyTaskResponse::from)
                .collect(Collectors.toList());
    }

    public List<EndStudyTaskResponse> endStudyTask(EndStudyTaskRequest endStudyTaskRequest, String userId) {
        List<Long> studyTaskNumbers = endStudyTaskRequest.getStudyTaskNumbers();
        List<EndStudyTaskResponse> endStudyTaskResponses = new ArrayList<>();

        studyTaskNumbers.forEach(studyTaskNumber -> {
            StudyTask studyTask = studyTaskRepository.findById(studyTaskNumber).orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다."));
            studyTask.chkStudyTaskState(userId);
            studyTask.endStudyTask(LocalDateTime.now());
            endStudyTaskResponses.add(EndStudyTaskResponse.from(studyTask));
        });

        return endStudyTaskResponses;
    }

    @Transactional(readOnly = true)
    public FindStudyTaskResponse findStudyTask(Long studyTaskNumber) {
        StudyTask studyTask = studyTaskRepository.findById(studyTaskNumber).orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다."));
        return FindStudyTaskResponse.from(studyTask);

    }

    @Transactional(readOnly = true)
    public List<FindStudyTaskResponse> findStudyTaskByUserId(Long studyTaskNumber) {
        List<StudyTask> studyTasks = studyTaskRepository.findAllById(studyTaskNumber);
        List<FindStudyTaskResponse> findStudyTaskResponses = new ArrayList<>();

        studyTasks.forEach(studyTask -> {
            FindStudyTaskResponse findStudyTaskResponse = FindStudyTaskResponse.from(studyTask);
            findStudyTaskResponses.add(findStudyTaskResponse);
        });

        return findStudyTaskResponses;

    }


}

