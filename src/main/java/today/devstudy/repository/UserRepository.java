package today.devstudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import today.devstudy.domain.User;

public interface UserRepository extends JpaRepository<User,Long>{

}
