package today.devstudy.service;

import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import today.devstudy.domain.StudyTask;
import today.devstudy.domain.Subject;
import today.devstudy.domain.User;
import today.devstudy.dto.studyTask.EndStudyTaskRequest;
import today.devstudy.dto.studyTask.EndStudyTaskResponse;
import today.devstudy.dto.studyTask.StartStudyTaskRequest;
import today.devstudy.dto.studyTask.StartStudyTaskResponse;
import today.devstudy.exception.NotAuthorityException;
import today.devstudy.exception.UserNotFoundException;
import today.devstudy.repository.StudyTaskRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StudyTaskServiceTest {
    private Logger log =  (Logger) LoggerFactory.getLogger(StudyTaskServiceTest.class);
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

        StartStudyTaskResponse response = studyTaskService.startStudyTask(request, user.getUserId());
        StudyTask studyTask = studyTaskRepository.findById(response.getStudyTaskNumber()).get();

        assertEquals(response.getStudyTaskNumber(),studyTask.getNumber());
    }

    @Test
    public void startStudyTaskExceptionTest() {
        User user = userService.create("dbtlwns", "simba0502@naver.com", "1234", "male");
        StartStudyTaskRequest request = new StartStudyTaskRequest(Subject.ALGORITHM);

        StartStudyTaskResponse response = studyTaskService.startStudyTask(request, user.getUserId());
        StudyTask studyTask = studyTaskRepository.findById(response.getStudyTaskNumber()).get();

        assertNotNull(studyTask);
    }

    @Test
    public void endStudyTaskTest() {
        User user = userService.create("dbtlwns", "simba0502@naver.com", "1234", "male");
        StartStudyTaskRequest startStudyTaskRequest = new StartStudyTaskRequest(Subject.ALGORITHM);
        StartStudyTaskResponse startStudyTaskResponse = studyTaskService.startStudyTask(startStudyTaskRequest,user.getUserId());
        studyTaskLogging(startStudyTaskResponse.getStudyTaskNumber(),"StartTask");

        EndStudyTaskRequest endStudyTaskRequest = new EndStudyTaskRequest(startStudyTaskResponse.getStudyTaskNumber());
        EndStudyTaskResponse endStudyTaskResponse = studyTaskService.endStudyTask(endStudyTaskRequest,user.getUserId());
        studyTaskLogging(endStudyTaskResponse.getStudyTaskNumber(),"EndTask");

        assertEquals(startStudyTaskResponse.getStudyTaskNumber(),endStudyTaskResponse.getStudyTaskNumber());
    }

    @Test
    public void endStudyTaskStateExceptionTest() {
        User user = userService.create("dbtlwns", "simba0502@naver.com", "1234", "male");
        StartStudyTaskRequest startStudyTaskRequest = new StartStudyTaskRequest(Subject.ALGORITHM);
        StartStudyTaskResponse startStudyTaskResponse = studyTaskService.startStudyTask(startStudyTaskRequest,user.getUserId());
        EndStudyTaskRequest endStudyTaskRequest = new EndStudyTaskRequest(startStudyTaskResponse.getStudyTaskNumber());
        studyTaskService.endStudyTask(endStudyTaskRequest,user.getUserId());

        assertThrows(IllegalStateException.class,()->studyTaskService.endStudyTask(endStudyTaskRequest,user.getUserId()));
    }

    @Test
    public void endStudyTaskAuthorityExceptionTest() {
        User user = userService.create("dbtlwns", "simba0502@naver.com", "1234", "male");
        StartStudyTaskRequest startStudyTaskRequest = new StartStudyTaskRequest(Subject.ALGORITHM);
        StartStudyTaskResponse startStudyTaskResponse = studyTaskService.startStudyTask(startStudyTaskRequest,user.getUserId());
        EndStudyTaskRequest endStudyTaskRequest = new EndStudyTaskRequest(startStudyTaskResponse.getStudyTaskNumber());
        String noneId = "";

        assertThrows(NotAuthorityException.class,()->studyTaskService.endStudyTask(endStudyTaskRequest,noneId));
    }

    private void studyTaskLogging(Long studyTaskNumber,String state){
        StudyTask studyTask = studyTaskRepository.findById(studyTaskNumber).get();
        log.info(state + " Start Time : " + studyTask.getStartTime());
        log.info(state + " End Time : " + studyTask.getEndTime());
    }
}