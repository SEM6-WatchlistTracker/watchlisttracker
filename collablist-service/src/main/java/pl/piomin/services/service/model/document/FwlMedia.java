package pl.piomin.services.service.model.document;

import java.util.Date;

import pl.piomin.services.service.model.enums.Status;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("media")
public class FwlMedia {
    @Id private String id;
    private String listId;
    private Integer mediaId;
    private String progression;
    private Status status;
    private Date lastUpdated;

    public String getId() {
        return id;
    }

    public String getListId() {
        return listId;
    }

    public Integer getMediaId() {
        return mediaId;
    }

    public String getProgression() {
        return progression;
    }
    public void setProgression(String progression) {
        this.progression = progression;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public FwlMedia() {}

    // For creating a new media
    public FwlMedia(String listId, Integer mediaId, String progression, Status status) {
        super();
        this.listId = listId;
        this.mediaId = mediaId;
        this.progression = progression;
        this.status = status;
        this.lastUpdated = new Date();
    }
}
