package pl.piomin.services.service.model.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
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

    public User(String displayName) { // without auth service
        super();
        this.displayName = displayName;
    }
}
