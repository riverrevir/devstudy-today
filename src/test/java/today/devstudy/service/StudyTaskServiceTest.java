package today.devstudy.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import today.devstudy.domain.studyTask.StudyTask;
import today.devstudy.domain.studyTask.StudyTaskRepository;
import today.devstudy.domain.type.Subject;
import today.devstudy.domain.user.User;
import today.devstudy.domain.user.UserRepository;
import today.devstudy.dto.studyTask.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

    @Autowired
    private UserRepository userRepository;

    private static User user;


    @BeforeAll
    public void init() {
        user = new User();
        user.setUserId("dbtlwns");
        user.setEmail("userEmail@naver.com");
        user.setSex("MALE");
        user.setPassword("1234");
        userRepository.save(user);
    }

    @Test
    public void startStudyTask() {
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
        String userId = "noneUser";
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
        List<Subject> subjects = new ArrayList<>();
        subjects.add(Subject.ALGORITHM);

        StartStudyTaskRequest startStudyTaskRequest = new StartStudyTaskRequest(subjects);
        List<StartStudyTaskResponse> startStudyTaskResponse1 = studyTaskService.startStudyTask(startStudyTaskRequest, user.getUserId());
        List<StartStudyTaskResponse> startStudyTaskResponse2 = studyTaskService.startStudyTask(startStudyTaskRequest, user.getUserId());

        assertNotEquals(startStudyTaskResponse1.get(0).getStudyTaskNumber(), startStudyTaskResponse2.get(0).getStudyTaskNumber());
    }

    @Test
    public void endStudyTaskStateExceptionTest() {

        List<Long> studyTaskNumbers = new ArrayList<>();
        studyTaskNumbers.add(0L);
        EndStudyTaskRequest endStudyTaskRequest = new EndStudyTaskRequest(studyTaskNumbers);

        assertThrows(IllegalArgumentException.class, () -> {
            studyTaskService.endStudyTask(endStudyTaskRequest, user.getUserId());
        });
    }

    @Test
    public void studyTaskEndTimeNotNullException() {
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

        assertThrows(IllegalArgumentException.class, () -> {
            studyTaskService.endStudyTask(endStudyTaskRequest, user.getUserId());
        });

    }

    @Test
    public void studyTaskForDayCompletedTest() {
        LocalDateTime today = LocalDateTime.now();
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

        int year = today.getYear();
        int month = today.getMonth().getValue();
        int day = today.getDayOfMonth();
        CountStudyTaskForDayResponse response = studyTaskService.countCompletedStudyForDay(year, month, day, user.getUserId());
        assertEquals(response.getCount(),subjects.size());
    }

    @Test
    public void studyTaskForDayOnGoingTest() {
        LocalDateTime today = LocalDateTime.now();
        List<Subject> subjects = new ArrayList<>();
        subjects.add(Subject.ALGORITHM);
        subjects.add(Subject.JAVA);
        StartStudyTaskRequest startStudyTaskRequest = new StartStudyTaskRequest(subjects);
        List<StartStudyTaskResponse> startStudyTaskResponse = studyTaskService.startStudyTask(startStudyTaskRequest, user.getUserId());

        int year = today.getYear();
        int month = today.getMonth().getValue();
        int day = today.getDayOfMonth();
        CountStudyTaskForDayResponse response = studyTaskService.countOnGoingStudyForDay(year, month, day, user.getUserId());
        assertEquals(response.getCount(),subjects.size());
    }

    @Test
    public void studyTaskForMonthTest() {
        LocalDateTime today = LocalDateTime.now();
        List<Subject> subjects = new ArrayList<>();
        subjects.add(Subject.ALGORITHM);
        subjects.add(Subject.JAVA);
        StartStudyTaskRequest startStudyTaskRequest = new StartStudyTaskRequest(subjects);
        List<StartStudyTaskResponse> startStudyTaskResponse = studyTaskService.startStudyTask(startStudyTaskRequest, user.getUserId());

        Map<Long, StudyTask> studyTaskMap = studyTaskRepository.findAll().stream()
                .collect(Collectors.toMap(StudyTask::getNumber, studyTask -> {
                    studyTask.startStudyTask(today.minusMonths(1L).plusHours(1L), user);
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
        List<FindStudyTaskResponse> findStudyTaskResponses = studyTaskService.findStudyForMonth(year, month, user.getUserId());
        findStudyTaskResponses.forEach(findStudyTaskResponse -> {
            Long number = findStudyTaskResponse.getNumber();
            assertTrue(studyTaskMap.containsKey(number));
        });
    }

    @Test
    public void studyTaskForYearTest() {
        LocalDateTime today = LocalDateTime.now();
        List<Subject> subjects = new ArrayList<>();
        subjects.add(Subject.ALGORITHM);
        subjects.add(Subject.JAVA);
        StartStudyTaskRequest startStudyTaskRequest = new StartStudyTaskRequest(subjects);
        List<StartStudyTaskResponse> startStudyTaskResponse = studyTaskService.startStudyTask(startStudyTaskRequest, user.getUserId());

        Map<Long, StudyTask> studyTaskMap = studyTaskRepository.findAll().stream()
                .collect(Collectors.toMap(StudyTask::getNumber, studyTask -> {
                    studyTask.startStudyTask(today.minusYears(1L).plusHours(1L), user);
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
        List<FindStudyTaskResponse> findStudyTaskResponses = studyTaskService.findStudyForYear(year, user.getUserId());
        findStudyTaskResponses.forEach(findStudyTaskResponse -> {
            Long number = findStudyTaskResponse.getNumber();
            assertTrue(studyTaskMap.containsKey(number));
        });
    }

    @Test
    public void autoKillStudyTaskTest() {
        LocalDateTime today = LocalDateTime.now();
        List<Subject> subjects = new ArrayList<>();
        subjects.add(Subject.ALGORITHM);
        subjects.add(Subject.JAVA);
        StartStudyTaskRequest startStudyTaskRequest = new StartStudyTaskRequest(subjects);
        List<StartStudyTaskResponse> startStudyTaskResponses = studyTaskService.startStudyTask(startStudyTaskRequest, user.getUserId());

        studyTaskRepository.findAll().forEach(studyTask -> {
            studyTask.startStudyTask(today.minusHours(13L), user);
        });

        studyTaskService.killStudyTask();
        startStudyTaskResponses.forEach(startStudyTaskResponse -> {
            Long number = startStudyTaskResponse.getStudyTaskNumber();
            assertTrue(studyTaskRepository.findById(number).isEmpty());
        } );

    }
}