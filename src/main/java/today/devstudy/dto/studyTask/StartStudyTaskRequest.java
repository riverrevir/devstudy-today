package today.devstudy.dto.studyTask;

import lombok.AllArgsConstructor;
import lombok.Getter;
import today.devstudy.domain.studyTask.StudyTask;
import today.devstudy.domain.type.Subject;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class StartStudyTaskRequest {
    @NotEmpty
    private List<Subject> subjects;

    public static List<StudyTask> newStudyTasks(StartStudyTaskRequest startStudyTaskRequest){
        List<Subject> subjects = startStudyTaskRequest.getSubjects();
        return subjects.stream().map(Subject::from).collect(Collectors.toList());
    }
}
