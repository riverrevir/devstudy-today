package today.devstudy.domain;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_username", unique = true)
    private String username;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_sex")
    private String sex;

    @Column(name = "user_email", unique = true)
    private String email;

    @OneToMany(mappedBy = "user")
    private List<StudyTask> studyTasks = new ArrayList<>();
}
