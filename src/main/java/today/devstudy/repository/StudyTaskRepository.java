package today.devstudy.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.devstudy.domain.StudyTask;

public interface StudyTaskRepository  extends JpaRepository<StudyTask,Long> {
    
}
