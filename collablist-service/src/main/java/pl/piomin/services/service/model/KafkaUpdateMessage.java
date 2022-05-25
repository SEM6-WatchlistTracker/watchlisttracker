package pl.piomin.services.service.model;

import pl.piomin.services.service.model.enums.Status;

import org.springframework.data.annotation.Id;

public class KafkaUpdateMessage {
    @Id private Integer mediaId;
    private Status statusToDecrease;
    private Status statusToIncrease;

    public Integer getMediaId() {
        return mediaId;
    }

    public Status getStatusToDecrease() {
        return statusToDecrease;
    }
    public void setStatusToDecrease(Status statusToDecrease) {
        this.statusToDecrease = statusToDecrease;
    }

    public Status getStatusToIncrease() {
        return statusToIncrease;
    }
    public void setStatusToIncrease(Status statusToIncrease) {
        this.statusToIncrease = statusToIncrease;
    }

    public KafkaUpdateMessage(Integer mediaId, Status statusToDecrease, Status statusToIncrease) {
        this.mediaId = mediaId;
        this.statusToDecrease = statusToDecrease;
        this.statusToIncrease = statusToIncrease;
    }

    @Override
    public String toString() {
        return this.mediaId + ";" + this.statusToDecrease + ";" + this.statusToIncrease;
    }
}
