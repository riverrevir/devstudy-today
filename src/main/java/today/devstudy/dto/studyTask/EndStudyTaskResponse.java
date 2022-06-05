package today.devstudy.dto.studyTask;

import lombok.Getter;
import today.devstudy.domain.StudyTask;

@Getter
public class EndStudyTaskResponse {
    private Long studyTaskNumber;
    public static EndStudyTaskResponse from(StudyTask studyTask){
        EndStudyTaskResponse response = new EndStudyTaskResponse();
        response.studyTaskNumber = studyTask.getNumber();
        return response;
    }
}
