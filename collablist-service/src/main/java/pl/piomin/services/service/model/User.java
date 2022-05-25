package pl.piomin.services.service.model;

import org.springframework.data.annotation.Id;

public class User {
    @Id private String userId;
    private String displayName;

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public User() {}

    public User(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }
}