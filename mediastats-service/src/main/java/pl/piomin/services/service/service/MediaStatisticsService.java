package pl.piomin.services.service.service;

import pl.piomin.services.service.model.document.MediaStatistic;
import pl.piomin.services.service.model.enums.Status;
import pl.piomin.services.service.repository.MediaStatisticRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MediaStatisticsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MediaStatisticsService.class);
    @Autowired private MediaStatisticRepository msRepository;

    public MediaStatistic getStatisticsOfMedia(Integer mediaId) {
        return msRepository.findById(mediaId).orElse(new MediaStatistic(mediaId, 0, 0, 0));
    }

    public void processMessage(String receivedMessage) {
        if (receivedMessage.contains(",")) {
            String[] messageParts = receivedMessage.split(",");
            for (String message : messageParts) { updateStatisticOfMedia(message); }
        }
        else updateStatisticOfMedia(receivedMessage);
    }

    private void updateStatisticOfMedia(String receivedMessage) {
        String[] messageParts = receivedMessage.split(";");
        Integer mediaId = Integer.parseInt(messageParts[0]);
        String tempStatusToDecrease = String.valueOf(messageParts[1]);
        String tempStatusToIncrease = String.valueOf(messageParts[2]);
        Status statusToDecrease = null; if (!tempStatusToDecrease.equals("null")) statusToDecrease = Status.valueOf(tempStatusToDecrease);
        Status statusToIncrease = null; if (!tempStatusToIncrease.equals("null")) statusToIncrease = Status.valueOf(tempStatusToIncrease);
        LOGGER.info("Updating statistics for media " + mediaId);

        MediaStatistic foundStat = msRepository.findById(mediaId).orElse(new MediaStatistic(mediaId, 0, 0, 0));
        if (statusToDecrease == null && statusToIncrease != null) { // add media to list
            foundStat.setAdded(foundStat.getAdded() + 1);
        }
        else if (statusToDecrease != null && statusToIncrease != null) { // update prevstatus to newstatus in list
            if (statusToDecrease == Status.Added) foundStat.setAdded(foundStat.getAdded() - 1);
            else if (statusToDecrease == Status.Watching) foundStat.setWatching(foundStat.getWatching() - 1);
            else if (statusToDecrease == Status.Watched) foundStat.setWatched(foundStat.getWatched() - 1);
            if (statusToIncrease == Status.Added) foundStat.setAdded(foundStat.getAdded() + 1);
            else if (statusToIncrease == Status.Watching) foundStat.setWatching(foundStat.getWatching() + 1);
            else if (statusToIncrease == Status.Watched) foundStat.setWatched(foundStat.getWatched() + 1);
        }
        else if (statusToDecrease != null && statusToIncrease == null) { // delete media from list
            if (statusToDecrease == Status.Added) foundStat.setAdded(foundStat.getAdded() - 1);
            else if (statusToDecrease == Status.Watching) foundStat.setWatching(foundStat.getWatching() - 1);
            else if (statusToDecrease == Status.Watched) foundStat.setWatched(foundStat.getWatched() - 1);
        }
        msRepository.save(foundStat);
    }
}
