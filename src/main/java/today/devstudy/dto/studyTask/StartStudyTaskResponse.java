package today.devstudy.dto.studyTask;

import lombok.Builder;
import lombok.Getter;
import today.devstudy.domain.studyTask.StudyTask;
import today.devstudy.domain.type.Subject;

import java.time.LocalDateTime;

@Builder
@Getter
public class StartStudyTaskResponse {
    private Long studyTaskNumber;
    private LocalDateTime startTime;
    private Subject subject;

    public static StartStudyTaskResponse from(StudyTask studyTask){
        Long studyTaskNumber = studyTask.getNumber();
        LocalDateTime startTime = studyTask.getStartTime();
        Subject subject = studyTask.getSubject();

        return StartStudyTaskResponse.builder()
                .studyTaskNumber(studyTaskNumber)
                .startTime(startTime)
                .subject(subject)
                .build();
    }

}
