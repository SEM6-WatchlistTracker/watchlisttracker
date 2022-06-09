package pl.piomin.services.service.model.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
public class AuthUser {
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

    public AuthUser() {}

    public AuthUser(String displayName) {
        super();
        this.displayName = displayName;
    }
}
