package pl.piomin.services.service.model.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import pl.piomin.services.service.model.Role;

@Document("users")
public class AuthUser {
    @Id private String userId;
    private String email;
    private String password;
    private Role role;

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public AuthUser() {}

    public AuthUser(String userId, String email, String password, Role role) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public AuthUser(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
