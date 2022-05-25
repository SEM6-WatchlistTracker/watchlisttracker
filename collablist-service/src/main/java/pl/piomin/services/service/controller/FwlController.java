package pl.piomin.services.service.controller;

import java.util.List;

import pl.piomin.services.service.model.FwlData;
import pl.piomin.services.service.model.FwlListModified;
import pl.piomin.services.service.model.FwlRequestModified;
import pl.piomin.services.service.model.document.FwlList;
import pl.piomin.services.service.model.document.FwlMedia;
import pl.piomin.services.service.model.document.FwlRequest;
import pl.piomin.services.service.service.FwlService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FwlController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FwlController.class);
    @Autowired private FwlService fwlService;

    // Lists \\

    @GetMapping() // ?userId={userId}
    public ResponseEntity<FwlData> getListsOfUser(@RequestParam String userId) {
        LOGGER.info("Getting lists of user " + userId);
        FwlData foundLists = fwlService.getListsOfUser(userId);
        return ResponseEntity.ok(foundLists);
    }

    @GetMapping("/{listId}") // ?userId={userId}
    public ResponseEntity<FwlListModified> getList(@PathVariable String listId, @RequestParam String userId) {
        LOGGER.info("Getting list " + listId + " of user " + userId);
        FwlListModified foundList = fwlService.getList(listId, userId);
        return ResponseEntity.ok(foundList);
    }

    @PostMapping("/create")
    public ResponseEntity<FwlList> createListAndSendRequest(@RequestBody FwlList newList) {
        LOGGER.info("Creating list and request.");
        FwlList createdList = fwlService.createListAndCreateRequest(newList);
        return ResponseEntity.ok(createdList);
    }

    @PutMapping("/update/{listId}")
    public ResponseEntity<FwlList> updateListName(@RequestBody FwlList newList, @PathVariable String listId) {
        LOGGER.info("Updating list " + listId);
        FwlList updatedList = fwlService.updateListName(newList, listId);
        return ResponseEntity.ok(updatedList);
    }

    @PutMapping("/leave/{listId}")  // ?userId={userId}
    public ResponseEntity<String> leaveList(@PathVariable String listId, @RequestParam String userId) {
        LOGGER.info("Updating list " + listId + ", user " + userId + " left");
        fwlService.leaveList(listId, userId);
        return ResponseEntity.ok("Left list.");
    }

    @DeleteMapping("/delete/{listId}")
    public ResponseEntity<String> deleteList(@PathVariable String listId) {
        LOGGER.info("Deleting list " + listId);
        fwlService.deleteListAndMedia(listId);
        return ResponseEntity.ok("Deleted list.");
    }

    // Requests \\

    @GetMapping("/requests") // ?userId={userId}
    public ResponseEntity<List<FwlRequestModified>> getReceivedRequestsOfUser(@RequestParam String userId) {
        LOGGER.info("Getting requests of user " + userId);
        List<FwlRequestModified> requests = fwlService.getReceivedRequestsOfUser(userId);
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/requests/send")
    public ResponseEntity<FwlRequest> createRequest(@RequestBody FwlList existingList) {
        LOGGER.info("Creating/re-sending request");
        FwlRequest createdRequest = fwlService.createRequest(existingList);
        return ResponseEntity.ok(createdRequest);
    }

    @PutMapping("/requests/accept/{listId}")
    public ResponseEntity<FwlListModified> acceptRequest(@PathVariable String listId) {
        LOGGER.info("Accepting request for list " + listId);
        FwlListModified updatedList = fwlService.acceptRequest(listId);
        return ResponseEntity.ok(updatedList);
    }

    @DeleteMapping("/requests/reject/{listId}")
    public ResponseEntity<String> rejectRequest(@PathVariable String listId) {
        LOGGER.info("Rejecting request for list " + listId);
        fwlService.deleteRequest(listId);
        return ResponseEntity.ok("Request rejected.");
    }

    // Media \\

    @GetMapping("/{listId}/media")
    public ResponseEntity<List<FwlMedia>> getMediaOfList(@PathVariable String listId) {
        LOGGER.info("Getting media for list " + listId);
        List<FwlMedia> foundMedia = fwlService.getAllMediaOfList(listId);
        return ResponseEntity.ok(foundMedia);
    }

    @GetMapping("/{listId}/media/{mediaId}")
    public ResponseEntity<FwlMedia> getMediaOfList(@PathVariable String listId, @PathVariable Integer mediaId) {
        LOGGER.info("Getting media " + mediaId + " of list " + listId);
        FwlMedia foundMedia = fwlService.getMediaOfList(listId, mediaId);
        return ResponseEntity.ok(foundMedia);
    }

    @PostMapping("/{listId}/media/add/{mediaId}")
    public ResponseEntity<FwlMedia> addMediaToList(@PathVariable String listId, @PathVariable Integer mediaId) {
        LOGGER.info("Adding media " + mediaId + " to list " + listId);
        FwlMedia createdMedia = fwlService.createMedia(listId, mediaId);
        return ResponseEntity.ok(createdMedia);
    }

    @PutMapping("/{listId}/media/update/{mediaId}")
    public ResponseEntity<FwlMedia> updateMediaStatusInList(@RequestBody FwlMedia newMedia, @PathVariable String listId, @PathVariable Integer mediaId) {
        LOGGER.info("Updating media " + mediaId + " of list " + listId);
        FwlMedia updatedMedia = fwlService.updateMediaStatus(newMedia, listId, mediaId);
        return ResponseEntity.ok(updatedMedia);
    }

    @DeleteMapping("/{listId}/media/remove/{mediaId}")
    public ResponseEntity<String> removeMediaFromList(@PathVariable String listId, @PathVariable Integer mediaId) {
        LOGGER.info("Deleting media " + mediaId + " of list " + listId);
        fwlService.deleteMedia(listId, mediaId);
        return ResponseEntity.ok("Media removed from list.");
    }
}
