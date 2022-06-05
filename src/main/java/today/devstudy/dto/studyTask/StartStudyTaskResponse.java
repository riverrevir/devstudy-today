package today.devstudy.dto.studyTask;

import lombok.Getter;
import today.devstudy.domain.StudyTask;

@Getter
public class StartStudyTaskResponse {
    private Long studyTaskNumber;
    public static StartStudyTaskResponse from(StudyTask studyTask){
        StartStudyTaskResponse response = new StartStudyTaskResponse();
        response.studyTaskNumber = studyTask.getNumber();
        return response;
    }

}
