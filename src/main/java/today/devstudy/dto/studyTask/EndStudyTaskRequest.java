package today.devstudy.dto.studyTask;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter
@AllArgsConstructor
public class EndStudyTaskRequest {
    @NotEmpty
    @Positive
    private List<Long> studyTaskNumbers;
}
