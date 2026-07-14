package vasilis.vasilis.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vasilis.vasilis.security.JwtResponse;
import vasilis.vasilis.user.DTO.ChangeRoleRequest;
import vasilis.vasilis.user.DTO.ChangeUsernameRequest;
import vasilis.vasilis.user.DTO.CreateUserRequest;
import vasilis.vasilis.user.DTO.UserRoleDTO;

import java.util.Map;


@RestController
@RequestMapping("/user")
@CrossOrigin(origins="*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            String token = userService.verify(user);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PutMapping("/change-username")
    public ResponseEntity<String> changeUsername(@RequestBody ChangeUsernameRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();

            userService.changeUsername(currentUsername, request.getUsername());
            return ResponseEntity.ok("Username updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating username: " + e.getMessage());
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            String currentPassword = request.get("currentPassword");
            String newPassword = request.get("newPassword");

            userService.changePassword(username, currentPassword, newPassword);
            return ResponseEntity.ok("Password updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating password: " + e.getMessage());
        }
    }

    @GetMapping("/role")
    public ResponseEntity<UserRoleDTO> getCurrentUserRole() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

            UserRoleDTO userRoleDTO = new UserRoleDTO();
            userRoleDTO.setId(user.getId());
            userRoleDTO.setUsername(user.getUsername());
            userRoleDTO.setRole(user.getRole());

            return ResponseEntity.ok(userRoleDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/change-role/{username}")
    public ResponseEntity<String> changeUserRole(
            @PathVariable String username,
            @RequestBody ChangeRoleRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();

            // Check if current user is admin
            Role currentUserRole = userService.getUserRole(currentUsername);
            if (!Role.ADMIN.equals(currentUserRole)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied. Only administrators can change user roles.");
            }

            userService.changeUserRole(username, request.getRole());
            return ResponseEntity.ok("User role updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating user role: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();

            // Check if current user is admin
            Role currentUserRole = userService.getUserRole(currentUsername);
            if (!Role.ADMIN.equals(currentUserRole)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied. Only administrators can create users.");
            }

            // Validate request
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is required");
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is required");
            }
            if (request.getRole() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role is required");
            }

            User newUser = userService.createUser(
                request.getUsername(),
                request.getPassword(),
                request.getRole()
            );

            UserRoleDTO response = new UserRoleDTO();
            response.setId(newUser.getId());
            response.setUsername(newUser.getUsername());
            response.setRole(newUser.getRole());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating user: " + e.getMessage());
        }
    }
}
