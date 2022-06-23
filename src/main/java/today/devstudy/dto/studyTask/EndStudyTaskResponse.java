package today.devstudy.dto.studyTask;

import lombok.Builder;
import lombok.Getter;
import today.devstudy.domain.studyTask.StudyTask;
import today.devstudy.domain.type.Subject;

import java.time.LocalDateTime;

@Builder
@Getter
public class EndStudyTaskResponse {
    private Long studyTaskNumber;
    private LocalDateTime endTime;
    private Subject subject;

    public static EndStudyTaskResponse from(StudyTask studyTask){
        Long studyTaskNumber = studyTask.getNumber();
        LocalDateTime endTime = studyTask.getEndTime();
        Subject subject = studyTask.getSubject();

        return EndStudyTaskResponse.builder()
                .studyTaskNumber(studyTaskNumber)
                .endTime(endTime)
                .subject(subject)
                .build();
    }
}
