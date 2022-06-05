package today.devstudy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.devstudy.domain.StudyTask;
import today.devstudy.domain.User;
import today.devstudy.dto.studyTask.EndStudyTaskRequest;
import today.devstudy.dto.studyTask.StartStudyTaskRequest;
import today.devstudy.dto.studyTask.StartStudyTaskResponse;
import today.devstudy.exception.UserNotFoundException;
import today.devstudy.repository.StudyTaskRepository;
import today.devstudy.repository.UserRepository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional
@Service
public class StudyTaskService {
    private final StudyTaskRepository studyTaskRepository;
    private final UserRepository userRepository;

    public StartStudyTaskResponse startStudyTask(StartStudyTaskRequest startStudyTaskRequest,String userId) {
        StudyTask studyTask = StartStudyTaskRequest.newStudyTask(startStudyTaskRequest);
        User user = userRepository.findByUserId(userId).orElseThrow(()-> new UserNotFoundException());
        studyTask.setUser(user);
        studyTask.setStartTime(LocalDateTime.now());
        studyTask = studyTaskRepository.save(studyTask);

        return StartStudyTaskResponse.from(studyTask);
    }
}

