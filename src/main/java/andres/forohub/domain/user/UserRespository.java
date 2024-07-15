package andres.forohub.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRespository extends JpaRepository<User, Long> {
    UserDetails findByUsername(String username);
}
