package pl.piomin.services.service.controller;

import pl.piomin.services.service.model.document.MediaStatistic;
import pl.piomin.services.service.service.MediaStatisticsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MediaStatisticsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MediaStatisticsController.class);
    @Autowired private MediaStatisticsService msService;

    @GetMapping("/get/{mediaId}")
    public ResponseEntity<MediaStatistic> getStatisticsOfMedia(@PathVariable Integer mediaId) {
        LOGGER.info("Getting media statistic " + mediaId);
        MediaStatistic foundStat = msService.getStatisticsOfMedia(mediaId);
        return ResponseEntity.ok(foundStat);
    }

    @KafkaListener(topics = "mediastatistics")
    public void updateStatistics(String receivedMessage) {
        LOGGER.info("Received request to update media statistic");
        msService.processMessage(receivedMessage);
    }
}
