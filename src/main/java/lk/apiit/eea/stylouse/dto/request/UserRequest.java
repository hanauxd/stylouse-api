package lk.apiit.eea.stylouse.dto.request;

import lk.apiit.eea.stylouse.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String role;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;

    public User getUser() {
        User user = new User();
        user.setRole(this.role);
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setPhone(this.phone);
        user.setEmail(this.email);
        user.setPassword(hashPassword(this.password));
        return user;
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
