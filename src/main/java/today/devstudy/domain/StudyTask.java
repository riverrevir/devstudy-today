package today.devstudy.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class StudyTask {

    @Id
    @GeneratedValue
    @Column(name = "studytask_id")
    private Long id;

    @Column(name = "studytask_subject")
    @Enumerated(EnumType.STRING)
    private Subject subject;

    @Column(name = "studytask_starttime")
    private LocalDateTime startTime;

    @Column(name = "studytask_endtime")
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
