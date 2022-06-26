package today.devstudy.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import today.devstudy.domain.studyTask.StudyTask;
import today.devstudy.domain.type.Subject;
import today.devstudy.domain.User;
import today.devstudy.dto.studyTask.*;
import today.devstudy.domain.studyTask.StudyTaskRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StudyTaskServiceTest {
    private Logger log = (Logger) LoggerFactory.getLogger(StudyTaskServiceTest.class);
    @Autowired
    private StudyTaskService studyTaskService;
    @Autowired
    private StudyTaskRepository studyTaskRepository;
    @Autowired
    private UserService userService;

    @Test
    public void startStudyTask() {
        User user = userService.create("dbtlwns", "simba0502@naver.com", "1234", "male");
        List<Subject> subjects = new ArrayList<>();
        subjects.add(Subject.ALGORITHM);
        subjects.add(Subject.JAVA);
        StartStudyTaskRequest request = new StartStudyTaskRequest(subjects);

        List<StartStudyTaskResponse> response = studyTaskService.startStudyTask(request, user.getUserId());
        response.forEach(startStudyTask -> {
            Long studyTaskNumber = startStudyTask.getStudyTaskNumber();
            StudyTask studyTask = studyTaskRepository.findById(studyTaskNumber).get();
            assertNotNull(studyTask);
        });


    }

    @Test
    public void startStudyTaskExceptionTest() {
        String userId = "dbtlwns";
        List<Subject> subjects = new ArrayList<>();
        subjects.add(Subject.ALGORITHM);
        subjects.add(Subject.JAVA);
        StartStudyTaskRequest request = new StartStudyTaskRequest(subjects);

        assertThrows(IllegalArgumentException.class, () -> {
            studyTaskService.startStudyTask(request, userId);
        });
    }

    @Test
    public void endStudyTaskTest() {
        User user = userService.create("dbtlwns", "simba0502@naver.com", "1234", "male");
        List<Subject> subjects = new ArrayList<>();
        subjects.add(Subject.ALGORITHM);
        subjects.add(Subject.JAVA);
        StartStudyTaskRequest startStudyTaskRequest = new StartStudyTaskRequest(subjects);
        List<StartStudyTaskResponse> startStudyTaskResponse = studyTaskService.startStudyTask(startStudyTaskRequest, user.getUserId());

        Map<Long, StudyTask> studyTaskMap = studyTaskRepository.findAll().stream()
                .collect(Collectors.toMap(StudyTask::getNumber, studyTask -> {
                    return studyTask;
                }));

        List<Long> studyTaskNumbers = startStudyTaskResponse.stream()
                .map(StartStudyTaskResponse::getStudyTaskNumber)
                .collect(Collectors.toList());
        EndStudyTaskRequest endStudyTaskRequest = new EndStudyTaskRequest(studyTaskNumbers);
        List<EndStudyTaskResponse> endStudyTaskResponse = studyTaskService.endStudyTask(endStudyTaskRequest, user.getUserId());

        endStudyTaskResponse.forEach(response -> {
            Long number = response.getStudyTaskNumber();
            StudyTask studyTask = studyTaskMap.get(number);
            assertEquals(response.getSubject(), studyTask.getSubject());
            assertEquals(response.getEndTime(), studyTask.getEndTime());
        });
    }

    @Test
    public void startStudySameTaskTest() {
        User user = userService.create("dbtlwns", "simba0502@naver.com", "1234", "male");
        List<Subject> subjects = new ArrayList<>();
        subjects.add(Subject.ALGORITHM);

        StartStudyTaskRequest startStudyTaskRequest = new StartStudyTaskRequest(subjects);
        List<StartStudyTaskResponse> startStudyTaskResponse1 = studyTaskService.startStudyTask(startStudyTaskRequest, user.getUserId());
        List<StartStudyTaskResponse> startStudyTaskResponse2 = studyTaskService.startStudyTask(startStudyTaskRequest, user.getUserId());

        assertNotEquals(startStudyTaskResponse1.get(0).getStudyTaskNumber(),startStudyTaskResponse2.get(0).getStudyTaskNumber());
    }

    @Test
    public void endStudyTaskStateExceptionTest() {
        User user = userService.create("dbtlwns", "simba0502@naver.com", "1234", "male");

        List<Long> studyTaskNumbers = new ArrayList<>();
        studyTaskNumbers.add(0L);
        EndStudyTaskRequest endStudyTaskRequest = new EndStudyTaskRequest(studyTaskNumbers);

        assertThrows(IllegalArgumentException.class, () -> {
            studyTaskService.endStudyTask(endStudyTaskRequest, user.getUserId());
        });
    }

    @Test
    public void studyTaskEndTimeNotNullException() {
        User user = userService.create("dbtlwns", "simba0502@naver.com", "1234", "male");
        List<Subject> subjects = new ArrayList<>();
        subjects.add(Subject.ALGORITHM);
        subjects.add(Subject.JAVA);
        StartStudyTaskRequest startStudyTaskRequest = new StartStudyTaskRequest(subjects);
        List<StartStudyTaskResponse> startStudyTaskResponse = studyTaskService.startStudyTask(startStudyTaskRequest, user.getUserId());

        List<Long> studyTaskNumbers = startStudyTaskResponse.stream()
                .map(StartStudyTaskResponse::getStudyTaskNumber)
                .collect(Collectors.toList());
        EndStudyTaskRequest endStudyTaskRequest = new EndStudyTaskRequest(studyTaskNumbers);
        studyTaskService.endStudyTask(endStudyTaskRequest, user.getUserId());

        assertThrows(IllegalArgumentException.class,()->{
            studyTaskService.endStudyTask(endStudyTaskRequest, user.getUserId());
        });

    }

    @Test
    public void studyTaskForDayTest(){
        LocalDateTime today = LocalDateTime.now();
        User user = userService.create("dbtlwns", "simba0502@naver.com", "1234", "male");
        List<Subject> subjects = new ArrayList<>();
        subjects.add(Subject.ALGORITHM);
        subjects.add(Subject.JAVA);
        StartStudyTaskRequest startStudyTaskRequest = new StartStudyTaskRequest(subjects);
        List<StartStudyTaskResponse> startStudyTaskResponse = studyTaskService.startStudyTask(startStudyTaskRequest, user.getUserId());

        Map<Long, StudyTask> studyTaskMap = studyTaskRepository.findAll().stream()
                .collect(Collectors.toMap(StudyTask::getNumber, studyTask -> {
                    return studyTask;
                }));

        List<Long> studyTaskNumbers = startStudyTaskResponse.stream()
                .map(StartStudyTaskResponse::getStudyTaskNumber)
                .collect(Collectors.toList());
        EndStudyTaskRequest endStudyTaskRequest = new EndStudyTaskRequest(studyTaskNumbers);
        studyTaskService.endStudyTask(endStudyTaskRequest, user.getUserId());

        int year = today.getYear();
        int month = today.getMonth().getValue();
        int day = today.getDayOfMonth();
        List<FindStudyTaskResponse> findStudyTaskResponses = studyTaskService.findStudyForDay(year,month,day,user.getUserId());
        findStudyTaskResponses.forEach(findStudyTaskResponse -> {
            Long number = findStudyTaskResponse.getNumber();
            assertTrue(studyTaskMap.containsKey(number));
        });
    }

    @Test
    public void studyTaskForMonthTest(){
        LocalDateTime today = LocalDateTime.now();
        User user = userService.create("dbtlwns", "simba0502@naver.com", "1234", "male");
        List<Subject> subjects = new ArrayList<>();
        subjects.add(Subject.ALGORITHM);
        subjects.add(Subject.JAVA);
        StartStudyTaskRequest startStudyTaskRequest = new StartStudyTaskRequest(subjects);
        List<StartStudyTaskResponse> startStudyTaskResponse = studyTaskService.startStudyTask(startStudyTaskRequest, user.getUserId());

        Map<Long, StudyTask> studyTaskMap = studyTaskRepository.findAll().stream()
                .collect(Collectors.toMap(StudyTask::getNumber, studyTask -> {
                    studyTask.startStudyTask(today.minusMonths(1L).plusHours(1L),user);
                    return studyTask;
                }));

        List<Long> studyTaskNumbers = startStudyTaskResponse.stream()
                .map(StartStudyTaskResponse::getStudyTaskNumber)
                .collect(Collectors.toList());
        EndStudyTaskRequest endStudyTaskRequest = new EndStudyTaskRequest(studyTaskNumbers);
        List<EndStudyTaskResponse> endStudyTaskResponses = studyTaskService.endStudyTask(endStudyTaskRequest, user.getUserId());
        endStudyTaskResponses.forEach(endStudyTaskResponse -> {
            Long studyTaskNumber = endStudyTaskResponse.getStudyTaskNumber();
            StudyTask studyTask = studyTaskRepository.findById(studyTaskNumber).get();
            studyTask.endStudyTask(today.minusMonths(1L).plusHours(3L));
        });
        int year = today.getYear();
        int month = today.getMonth().getValue();
        List<FindStudyTaskResponse> findStudyTaskResponses = studyTaskService.findStudyForMonth(year,month,user.getUserId());
        findStudyTaskResponses.forEach(findStudyTaskResponse -> {
            Long number = findStudyTaskResponse.getNumber();
            assertTrue(studyTaskMap.containsKey(number));
        });
    }

    @Test
    public void studyTaskForYearTest(){
        LocalDateTime today = LocalDateTime.now();
        User user = userService.create("dbtlwns", "simba0502@naver.com", "1234", "male");
        List<Subject> subjects = new ArrayList<>();
        subjects.add(Subject.ALGORITHM);
        subjects.add(Subject.JAVA);
        StartStudyTaskRequest startStudyTaskRequest = new StartStudyTaskRequest(subjects);
        List<StartStudyTaskResponse> startStudyTaskResponse = studyTaskService.startStudyTask(startStudyTaskRequest, user.getUserId());

        Map<Long, StudyTask> studyTaskMap = studyTaskRepository.findAll().stream()
                .collect(Collectors.toMap(StudyTask::getNumber, studyTask -> {
                    studyTask.startStudyTask(today.minusYears(1L).plusHours(1L),user);
                    return studyTask;
                }));

        List<Long> studyTaskNumbers = startStudyTaskResponse.stream()
                .map(StartStudyTaskResponse::getStudyTaskNumber)
                .collect(Collectors.toList());
        EndStudyTaskRequest endStudyTaskRequest = new EndStudyTaskRequest(studyTaskNumbers);
        List<EndStudyTaskResponse> endStudyTaskResponses = studyTaskService.endStudyTask(endStudyTaskRequest, user.getUserId());
        endStudyTaskResponses.forEach(endStudyTaskResponse -> {
            Long studyTaskNumber = endStudyTaskResponse.getStudyTaskNumber();
            StudyTask studyTask = studyTaskRepository.findById(studyTaskNumber).get();
            studyTask.endStudyTask(today.minusYears(1L).plusHours(3L));
        });
        int year = today.getYear();
        List<FindStudyTaskResponse> findStudyTaskResponses = studyTaskService.findStudyForYear(year,user.getUserId());
        findStudyTaskResponses.forEach(findStudyTaskResponse -> {
            Long number = findStudyTaskResponse.getNumber();
            assertTrue(studyTaskMap.containsKey(number));
        });
    }
    private void studyTaskLogging(Long studyTaskNumber, String state) {
        StudyTask studyTask = studyTaskRepository.findById(studyTaskNumber).get();
        log.info(state + " Start Time : " + studyTask.getStartTime());
        log.info(state + " End Time : " + studyTask.getEndTime());
    }
}