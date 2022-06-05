package today.devstudy.dto.studyTask;

import lombok.AllArgsConstructor;
import lombok.Getter;
import today.devstudy.domain.Subject;

@Getter
@AllArgsConstructor
public class EndStudyTaskRequest {
    Long studyTaskNumber;
}
