package pl.piomin.services.service.model;

import java.util.Date;

import org.springframework.data.annotation.Id;

public class FwlListModified {
    @Id private String listId;
    private String listName;
    private User collaborator;
    private Date lastUpdated;
    // could add "open request"

    public String getListId() {
        return listId;
    }

    public String getListName() {
        return listName;
    }
    public void setListName(String listName) {
        this.listName = listName;
    }

    public User getCollaborator() {
        return collaborator;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public FwlListModified(String listId, String listName, User collaborator, Date lastUpdated) {
        this.listId = listId;
        this.listName = listName;
        this.collaborator = collaborator;
        this.lastUpdated = lastUpdated;
    }
}
