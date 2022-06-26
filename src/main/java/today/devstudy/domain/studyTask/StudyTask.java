package today.devstudy.domain.studyTask;

import lombok.Getter;
import today.devstudy.domain.user.User;
import today.devstudy.domain.type.Subject;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
public class StudyTask {

    @Id
    @GeneratedValue
    @Column(name = "studytask_number")
    private Long number;

    @Column(name = "studytask_subject")
    @Enumerated(EnumType.STRING)
    private Subject subject;

    @Column(name = "studytask_starttime")
    private LocalDateTime startTime;

    @Column(name = "studytask_endtime")
    private LocalDateTime endTime;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    protected StudyTask() {

    }

    public StudyTask(Subject subject) {
        this.subject = subject;
    }

    public void startStudyTask(LocalDateTime startTime, User user) {
        this.startTime = startTime;
        this.user = user;
    }

    public void endStudyTask(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void chkStudyTaskState(String userId) {
        if (isEndTimeNotNull()) {
            throw new IllegalArgumentException("이미 종료된 Task 입니다.");
        }
        if (!isRightAuthority(userId)) {
            throw new IllegalArgumentException("유저 권한이 올바르지 않습니다.");
        }
    }

    public boolean isRightAuthority(String userId) {
        return this.getUser().getUserId().equals(userId);
    }

    public boolean isEndTimeNotNull() {
        return this.getEndTime() == null ? false : true;
    }
}
