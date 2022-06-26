package today.devstudy.dto.studyTask;

import lombok.Builder;
import lombok.Getter;
import today.devstudy.domain.studyTask.StudyTask;
import today.devstudy.domain.type.Subject;

import java.time.LocalDateTime;

@Builder
@Getter
public class FindStudyTaskResponse {
    private Long number;
    private Subject subject;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public static FindStudyTaskResponse from(StudyTask studyTask){
        Long number = studyTask.getNumber();
        Subject subject = studyTask.getSubject();
        LocalDateTime startTime = studyTask.getStartTime();
        LocalDateTime endTime = studyTask.getEndTime();

        return FindStudyTaskResponse.builder()
                .number(number)
                .subject(subject)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}
