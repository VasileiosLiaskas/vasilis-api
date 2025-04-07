package vasilis.vasilis.user;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}
