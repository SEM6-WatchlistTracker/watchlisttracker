package pl.piomin.services.service.model.document;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("requests")
public class FwlRequest {
    @Id private String listId; // equal to FwlList
    private String listName;
    @Id private String ownerId;
    @Id private String memberId;
    private Date sentOn;

    public String getListId() {
        return listId;
    }

    public String getListName() {
        return listName;
    }
    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getMemberId() {
        return memberId;
    }
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
    
    public Date getSentOn() {
        return sentOn;
    }
    public void setSentOn(Date sentOn) {
        this.sentOn = sentOn;
    }

    public FwlRequest() {}

    // For creating a new request
    public FwlRequest(String listId, String listName, String ownerId, String memberId) {
        super();
        this.listId = listId;
        this.listName = listName;
        this.ownerId = ownerId;
        this.memberId = memberId;
        this.sentOn = new Date();
    }
}
