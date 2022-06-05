package today.devstudy.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long>{
    /**
     * 회원아이디찾기
     * @param username
     * @return
     */
    User findByusername(String username);
}
