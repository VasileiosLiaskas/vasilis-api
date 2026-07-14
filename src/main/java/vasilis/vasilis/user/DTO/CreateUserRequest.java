package vasilis.vasilis.user.DTO;

import lombok.Data;
import vasilis.vasilis.user.Role;

@Data
public class CreateUserRequest {
    private String username;
    private String password;
    private Role role;
}

