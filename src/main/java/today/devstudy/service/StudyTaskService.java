package today.devstudy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.devstudy.domain.StudyTask;
import today.devstudy.domain.User;
import today.devstudy.dto.studyTask.EndStudyTaskRequest;
import today.devstudy.dto.studyTask.EndStudyTaskResponse;
import today.devstudy.dto.studyTask.StartStudyTaskRequest;
import today.devstudy.dto.studyTask.StartStudyTaskResponse;
import today.devstudy.exception.NotAuthorityException;
import today.devstudy.exception.StudyTaskNotFoundException;
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
        User user = userRepository.findByUserId(userId).orElseThrow(()-> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        studyTask.setUser(user);
        studyTask.setStartTime(LocalDateTime.now());
        studyTask = studyTaskRepository.save(studyTask);

        return StartStudyTaskResponse.from(studyTask);
    }

    public EndStudyTaskResponse endStudyTask(EndStudyTaskRequest endStudyTaskRequest,String userId) {
        Long studyTaskNumber = endStudyTaskRequest.getStudyTaskNumber();
        StudyTask studyTask = studyTaskRepository.findById(studyTaskNumber).orElseThrow(()->new StudyTaskNotFoundException("잘못된 요청입니다."));
        chkStudyTaskState(studyTask,userId);
        studyTask.setEndTime(LocalDateTime.now());
        return EndStudyTaskResponse.from(studyTask);
    }
    private void chkStudyTaskState(StudyTask studyTask,String userId){
        if(isEndTimeNotNull(studyTask)){
            throw new IllegalStateException("이미 종료된 Task 입니다.");
        }
        if(!isRightAuthority(studyTask.getUser().getUserId(),userId)){
            throw new NotAuthorityException("유저 권한이 올바르지 않습니다.");
        }
    }
    private boolean isRightAuthority(String studyTaskId, String jwtTokenId){
        return studyTaskId.equals(jwtTokenId);
    }

    private boolean isEndTimeNotNull(StudyTask studyTask){
        return studyTask.getEndTime() == null ? false : true;
    }
}

