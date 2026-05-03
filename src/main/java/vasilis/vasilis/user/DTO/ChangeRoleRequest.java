package vasilis.vasilis.user.DTO;

import lombok.Data;
import vasilis.vasilis.user.Role;

@Data
public class ChangeRoleRequest {
    private Role role;
}
