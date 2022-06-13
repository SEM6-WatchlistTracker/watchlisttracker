package pl.piomin.services.service.model.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("stats")
public class MediaStatistic {
    @Id private Integer mediaId;
    private Integer added;
    private Integer watching;
    private Integer watched;

    public Integer getMediaId() {
        return mediaId;
    }
    
    public Integer getAdded() {
        return added;
    }
    public void setAdded(Integer added) {
        this.added = added;
    }

    public Integer getWatching() {
        return watching;
    }
    public void setWatching(Integer watching) {
        this.watching = watching;
    }

    public Integer getWatched() {
        return watched;
    }
    public void setWatched(Integer watched) {
        this.watched = watched;
    }

    public MediaStatistic() {}

    // For creating a new statistic
    public MediaStatistic(Integer mediaId, Integer added, Integer watching, Integer watched) {
        super();
        this.mediaId = mediaId;
        this.added = added;
        this.watching = watching;
        this.watched = watched;
    }
}
