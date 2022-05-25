package pl.piomin.services.service.model;

import java.util.Date;

import org.springframework.data.annotation.Id;

public class FwlRequestModified {
    @Id private String listId;
    private String listName;
    private User owner;
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

    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Date getSentOn() {
        return sentOn;
    }
    public void setSentOn(Date sentOn) {
        this.sentOn = sentOn;
    }

    public FwlRequestModified(String listId, String listName, User owner, Date sentOn) {
        this.listId = listId;
        this.listName = listName;
        this.owner = owner;
        this.sentOn = sentOn;
    }
}
