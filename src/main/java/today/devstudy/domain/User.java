package today.devstudy.domain;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_number")
    private Long number;

    @Column(name = "user_userId", unique = true)
    private String userId;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_sex")
    private String sex;

    @Column(name = "user_email", unique = true)
    private String email;

    @OneToMany(mappedBy = "user")
    private List<StudyTask> studyTasks = new ArrayList<>();
}
