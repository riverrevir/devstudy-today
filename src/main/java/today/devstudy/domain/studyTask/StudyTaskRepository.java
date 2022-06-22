package today.devstudy.domain.studyTask;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudyTaskRepository  extends JpaRepository<StudyTask,Long> {
    @Query("SELECT st FROM StudyTask st WHERE st.number = :studyTaskNumber")
    List<StudyTask> findAllById(Long studyTaskNumber);
}
