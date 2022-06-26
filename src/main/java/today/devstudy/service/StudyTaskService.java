package today.devstudy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
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
    private static final Long criterionHour = 12L;

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
    public List<FindStudyTaskResponse> findStudyForDay(int year, int month, int day, String userId) {
        LocalDateTime from = LocalDateTime.of(year, month, day, 0, 0, 0);
        LocalDateTime to = from.plusDays(1L).minusSeconds(1L);

        List<StudyTask> studyTasks = studyTaskRepository.findAllByStartTimeBetweenAndUserUserIdEquals(from, to, userId);

        return studyTasks.stream()
                .map(FindStudyTaskResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FindStudyTaskResponse> findStudyForMonth(int year, int month, String userId) {
        LocalDateTime from = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime to = from.plusMonths(1L).minusSeconds(1L);

        List<StudyTask> studyTasks = studyTaskRepository.findAllByStartTimeBetweenAndUserUserIdEquals(from, to, userId);

        return studyTasks.stream()
                .map(FindStudyTaskResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FindStudyTaskResponse> findStudyForYear(int year, String userId) {
        LocalDateTime from = LocalDateTime.of(year, 1, 1, 0, 0, 0);
        LocalDateTime to = from.plusYears(1L).minusSeconds(1L);

        List<StudyTask> studyTasks = studyTaskRepository.findAllByStartTimeBetweenAndUserUserIdEquals(from, to, userId);

        return studyTasks.stream()
                .map(FindStudyTaskResponse::from)
                .collect(Collectors.toList());
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void killStudyTask() {
        LocalDateTime criterionTime = LocalDateTime.now().minusHours(criterionHour);
        List<StudyTask> studyTasks = studyTaskRepository.findAllByStartTimeLessThan(criterionTime);
        studyTasks.forEach(studyTask -> {
            studyTaskRepository.delete(studyTask);
        });
    }

}

