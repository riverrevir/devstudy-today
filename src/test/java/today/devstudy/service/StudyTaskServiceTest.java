package today.devstudy.service;

import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import today.devstudy.domain.StudyTask;
import today.devstudy.domain.Subject;
import today.devstudy.domain.User;
import today.devstudy.dto.studyTask.StartStudyTaskRequest;
import today.devstudy.dto.studyTask.StartStudyTaskResponse;
import today.devstudy.exception.UserNotFoundException;
import today.devstudy.repository.StudyTaskRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class StudyTaskServiceTest {
    @Autowired
    private StudyTaskService studyTaskService;
    @Autowired
    private StudyTaskRepository studyTaskRepository;
    @Autowired
    private UserService userService;

    @Test
    public void startStudyTask() {
        User user = userService.create("dbtlwns", "simba0502@naver.com", "1234", "male");
        StartStudyTaskRequest request = new StartStudyTaskRequest(Subject.ALGORITHM);

        StartStudyTaskResponse response = studyTaskService.startStudyTask(request, user.getId());
        StudyTask studyTask = studyTaskRepository.findById(response.getStudyTaskId()).get();

        assertEquals(response.getStudyTaskId(),studyTask.getId());
    }

    @Test
    public void startStudyTaskExceptionTest() {
        StartStudyTaskRequest request = new StartStudyTaskRequest(Subject.ALGORITHM);
        Long noneId = 0l;

        assertThrows(UserNotFoundException.class,()->studyTaskService.startStudyTask(request, noneId));
    }
}