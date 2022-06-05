package today.devstudy.dto.studyTask;

import lombok.AllArgsConstructor;
import lombok.Getter;
import today.devstudy.domain.StudyTask;
import today.devstudy.domain.Subject;

@Getter
@AllArgsConstructor
public class StartStudyTaskRequest {
    private Subject subject;

    public static StudyTask newStudyTask(StartStudyTaskRequest startStudyTaskRequest){
        StudyTask studyTask = new StudyTask();
        studyTask.setSubject(startStudyTaskRequest.getSubject());
        return studyTask;
    }
}
