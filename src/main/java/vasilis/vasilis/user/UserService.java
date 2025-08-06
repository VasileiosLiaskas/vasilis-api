package vasilis.vasilis.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
//    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    String verify(User user);
}
