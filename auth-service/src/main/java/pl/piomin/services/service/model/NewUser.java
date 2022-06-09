package pl.piomin.services.service.model;

import org.springframework.data.annotation.Id;

public class NewUser {
    @Id private String userId;
    private String token;

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public NewUser(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}
