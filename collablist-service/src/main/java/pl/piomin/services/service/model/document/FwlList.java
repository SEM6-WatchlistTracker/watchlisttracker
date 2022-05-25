package pl.piomin.services.service.model.document;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("lists")
public class FwlList {
    @Id private String listId;
    private String listName;
    @Id private String ownerId;
    @Id private String memberId;
    private Date lastUpdated;

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
    
    public Date getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public FwlList() {}
    
    // For creating a new list
    public FwlList(String listName, String ownerId, String memberId) {
        super();
        this.listName = listName;
        this.ownerId = ownerId;
        this.memberId = memberId;
        this.lastUpdated = new Date();
    }
}
