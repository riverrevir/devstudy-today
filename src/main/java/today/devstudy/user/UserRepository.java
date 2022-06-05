package today.devstudy.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>{
    /**
     * 회원아이디찾기
     * @param userName
     * @return
     */
    Optional<User> findByusername(String username);
}
