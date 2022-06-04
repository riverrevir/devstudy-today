package today.devstudy.dto.studyTask;

import lombok.Getter;
import today.devstudy.domain.StudyTask;

@Getter
public class StartStudyTaskResponse {
    private Long studyTaskId;
    public static StartStudyTaskResponse from(StudyTask studyTask){
        StartStudyTaskResponse response = new StartStudyTaskResponse();
        response.studyTaskId = studyTask.getId();
        return response;
    }

}
