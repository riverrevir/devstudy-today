package today.devstudy.domain.studyTask;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StudyTaskRepository  extends JpaRepository<StudyTask,Long> {

    List<StudyTask> findAllByStartTimeBetweenAndEndTimeNotNullAndUserUserIdEquals(LocalDateTime from, LocalDateTime to, String userId);

    long countByStartTimeBetweenAndEndTimeNotNullAndUserUserIdEquals(LocalDateTime from, LocalDateTime to, String userId);

    long countByStartTimeBetweenAndEndTimeIsNullAndUserUserIdEquals(LocalDateTime from, LocalDateTime to, String userId);

    List<StudyTask> findAllByStartTimeLessThan(LocalDateTime criterionTime);
}