package vasilis.vasilis.user;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
//    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    String verify(User user);

    void changeUsername(String currentUsername, String newUsername);

    void changePassword(String username, String currentPassword, String newPassword);

    void changeUserRole(String username, Role role);

    Role getUserRole(String username);
}
