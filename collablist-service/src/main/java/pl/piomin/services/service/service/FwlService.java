package pl.piomin.services.service.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.piomin.services.service.model.FwlData;
import pl.piomin.services.service.model.FwlListModified;
import pl.piomin.services.service.model.FwlRequestModified;
import pl.piomin.services.service.model.KafkaUpdateMessage;
import pl.piomin.services.service.model.enums.Status;
import pl.piomin.services.service.model.User;
import pl.piomin.services.service.model.document.FwlList;
import pl.piomin.services.service.model.document.FwlMedia;
import pl.piomin.services.service.model.document.FwlRequest;
import pl.piomin.services.service.repository.FwlListRepository;
import pl.piomin.services.service.repository.FwlMediaRepository;
import pl.piomin.services.service.repository.FwlRequestRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class FwlService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FwlService.class);
    @Autowired private FwlListRepository fwlListRepository;
    @Autowired private FwlRequestRepository fwlRequestRepository;
    @Autowired private FwlMediaRepository fwlMediaRepository;
    @Autowired private KafkaTemplate<String, String> kafka;

    // Lists \\

    public FwlData getListsOfUser(String userId) {
        List<FwlList> foundLists = fwlListRepository.findAllByOwnerIdOrMemberId(userId, userId);
        if (!foundLists.isEmpty()) {
            List<FwlListModified> modifiedLists = modifyLists(foundLists, userId);
            Integer hasRequests = fwlRequestRepository.countByMemberId(userId);
            FwlData fwlData = new FwlData(modifiedLists, hasRequests);
            return fwlData;
        }
        return null;
    }

    private List<FwlListModified> modifyLists(List<FwlList> foundLists, String userId) {
        List<String> usedIds = new ArrayList<>();
        List<User> foundUsers = new ArrayList<>(); // adds user to a list to reduce load
        for (FwlList list : foundLists) {
            User foundUser = new User();
            String collaboratorId = new String();
            if (!list.getOwnerId().equals(userId)) collaboratorId = list.getOwnerId();
            if (list.getMemberId() != null) if (!list.getMemberId().equals(userId)) collaboratorId = list.getMemberId();
            if (!collaboratorId.equals("")) {
                if (!usedIds.contains(collaboratorId)) {
                    foundUser = getUser(collaboratorId);
                    foundUsers.add(foundUser);
                    usedIds.add(collaboratorId);
                }
            }
        }
        List<FwlListModified> lists = new ArrayList<>();
        for (FwlList list : foundLists) {
            User collaborator = new User();
            for (User user : foundUsers) {
                if (list.getMemberId() != null) { 
                    if (list.getOwnerId().equals(user.getUserId()) || list.getMemberId().equals(user.getUserId())) collaborator = user;
                }
                else collaborator = null;
            }
            FwlListModified modifiedList = new FwlListModified(list.getListId(), list.getListName(), collaborator, list.getLastUpdated());
            lists.add(modifiedList);
        }
        return lists;
    }

    private User getUser(String userId) {
        User user = new User(userId, "Could not be retrieved.");
        try {
            WebClient client = WebClient.create("https://watchlisttracker-polishedstudios.cloud.okteto.net/");
            Mono<User> response = client.get()
                .uri("/users/get/" + userId)
                .retrieve()
                .bodyToMono(User.class);
            User foundUser = response.block();
            user.setDisplayName(foundUser.getDisplayName());
        } catch (Exception e) { LOGGER.info("Could not reach user service.");} 
        return user;
    }

    public FwlListModified getList(String listId, String userId) {
        FwlList foundList = fwlListRepository.findById(listId).orElse(null);
        if (foundList != null) {
            if (foundList.getOwnerId().equals(userId) || foundList.getMemberId().equals(userId)) {
                String collaboratorId = new String();
                if (!foundList.getOwnerId().equals(userId)) collaboratorId = foundList.getOwnerId();
                if (foundList.getMemberId() != null) if (!foundList.getMemberId().equals(userId)) collaboratorId = foundList.getMemberId();
                User collaborator = new User();
                if (!collaboratorId.equals("")) collaborator = getUser(collaboratorId);
                else collaborator = null;
                FwlListModified modifiedList = new FwlListModified(foundList.getListId(), foundList.getListName(), collaborator, foundList.getLastUpdated());
                return modifiedList;
            }
        }
        return null;
    }

    public FwlList updateListName(FwlList updatedList, String listId) {
        FwlList list = fwlListRepository.findById(listId).orElse(null);
        list.setListName(updatedList.getListName());
        list.setLastUpdated(new Date());
        return fwlListRepository.save(list);
    }

    public FwlList createListAndCreateRequest(FwlList newList) {
        FwlList list = new FwlList(newList.getListName(), newList.getOwnerId(), null);
        FwlList savedList = fwlListRepository.save(list);
        FwlList requestList = savedList;
        requestList.setMemberId(newList.getMemberId());
        createRequest(requestList);
        return savedList;
    }

    public void leaveList(String listId, String userId) {
        FwlList foundList = fwlListRepository.findById(listId).orElse(null);
        if (foundList != null) {
            String role = "";
            if (foundList.getOwnerId().equals(userId)) role = "owner";
            if (foundList.getMemberId() != null) if (foundList.getMemberId().equals(userId)) role = "member";
            if (role == "owner") {
                if (foundList.getMemberId() != null) {
                    foundList.setOwnerId(foundList.getMemberId());
                    foundList.setMemberId(null);
                    fwlListRepository.save(foundList);
                }
                else deleteListAndMedia(listId);
            }
            else if (role == "member") {
                foundList.setMemberId(null);
                fwlListRepository.save(foundList);
            }
            if (role != "") deleteRequest(listId);
        }
    }

    public void deleteListAndMedia(String listId) {
        FwlList foundList = fwlListRepository.findById(listId).orElse(null);
        if (foundList != null) {
            List<FwlMedia> foundMedia = getAllMediaOfList(listId);
            if (!foundMedia.isEmpty()) {
                String bulkMessage = ""; Boolean notFirst = false;
                for (FwlMedia media : foundMedia) {
                    KafkaUpdateMessage message = new KafkaUpdateMessage(media.getMediaId(), media.getStatus(), null);
                    if (notFirst) bulkMessage += ",";
                    bulkMessage += message.toString();
                    notFirst = true;
                }
                try { this.kafka.send("mediastatistics", bulkMessage.toString()); } 
                catch (Exception e) { LOGGER.info("Kafka offline."); }
                fwlMediaRepository.deleteAllByListId(listId);
            }
            deleteRequest(listId);
            fwlListRepository.delete(foundList);
        }
    }

    // Requests \\

    public FwlRequest createRequest(FwlList newList) {
        FwlRequest existingRequest = fwlRequestRepository.findById(newList.getListId()).orElse(null);
        FwlRequest savedRequest = new FwlRequest();
        if (existingRequest == null)  {
            FwlRequest request = new FwlRequest(newList.getListId(), newList.getListName(), newList.getOwnerId(), newList.getMemberId());
            savedRequest = fwlRequestRepository.save(request); }
        else {
            existingRequest.setMemberId(newList.getMemberId());
            existingRequest.setSentOn(new Date());
            savedRequest = fwlRequestRepository.save(existingRequest); }
        return savedRequest;
    }

    public List<FwlRequestModified> getReceivedRequestsOfUser(String userId) {
        List<FwlRequest> foundRequests = fwlRequestRepository.findByMemberId(userId);
        if (!foundRequests.isEmpty()) {
            List<FwlRequestModified> modifiedRequests = modifyRequests(foundRequests, userId);
            return modifiedRequests;
        }
        return null;
    }

    private List<FwlRequestModified> modifyRequests(List<FwlRequest> foundRequests, String userId) {
        List<User> foundUsers = new ArrayList<>(); // adds user to a list to reduce load
        for (FwlRequest request : foundRequests) {
            User foundUser = getUser(request.getOwnerId());
            foundUsers.add(foundUser);
        }
        List<FwlRequestModified> requests = new ArrayList<>();
        for (FwlRequest request : foundRequests) {
            User owner = new User();
            for (User user : foundUsers) {
                if (request.getOwnerId().equals(user.getUserId())) owner = user;
            }
            FwlRequestModified modifiedRequest = new FwlRequestModified(request.getListId(), request.getListName(), owner, request.getSentOn());
            requests.add(modifiedRequest);
        }
        return requests;
    }

    public FwlListModified acceptRequest(String listId) {
        FwlRequest foundRequest = fwlRequestRepository.findById(listId).orElse(null);
        FwlList foundList = fwlListRepository.findById(listId).orElse(null);
        if (foundRequest != null) {
            FwlList savedList = new FwlList();
            if (foundList != null) {
                foundList.setMemberId(foundRequest.getMemberId());
                foundList.setLastUpdated(new Date());
                savedList = fwlListRepository.save(foundList);
            }
            else {
                FwlList newList = new FwlList(foundRequest.getListName(), foundRequest.getOwnerId(), foundRequest.getMemberId());
                savedList = fwlListRepository.save(newList);
            }
            fwlRequestRepository.delete(foundRequest);
            User owner = getUser(savedList.getOwnerId());
            FwlListModified modifiedList = new FwlListModified(savedList.getListId(), savedList.getListName(), owner, savedList.getLastUpdated());
            return modifiedList;
        }
        return null;
    }

    public void deleteRequest(String listId) {
        fwlRequestRepository.deleteById(listId);
    }

    // Media \\

    public List<FwlMedia> getAllMediaOfList(String listId) {
        List<FwlMedia> foundMedia = fwlMediaRepository.findByListId(listId);
        return foundMedia;
    }

    public FwlMedia getMediaOfList(String listId, Integer mediaId) {
        FwlMedia media = fwlMediaRepository.findByListIdAndMediaId(listId, mediaId);
        if (media != null) return media;
        return null;
    }

    public FwlMedia createMedia(String listId, Integer mediaId) {
        FwlMedia existingMedia = fwlMediaRepository.findByListIdAndMediaId(listId, mediaId);
        if (existingMedia == null) {
            FwlMedia media = new FwlMedia(listId, mediaId, null, Status.Added);
            FwlMedia savedMedia = fwlMediaRepository.save(media);
            KafkaUpdateMessage message = new KafkaUpdateMessage(mediaId, null, Status.Added);
            try { this.kafka.send("mediastatistics", message.toString()); } 
            catch (Exception e) { LOGGER.info("Kafka offline."); }
            return savedMedia;
        }
        return null;
    }

    public FwlMedia updateMediaStatus(FwlMedia updatedMedia, String listId, Integer mediaId) {
        FwlMedia media = fwlMediaRepository.findByListIdAndMediaId(listId, mediaId);
        if (media != null) {
            if (updatedMedia.getStatus() != media.getStatus()) {
                Status previousStatus = media.getStatus();
                media.setStatus(updatedMedia.getStatus());
                media.setLastUpdated(new Date());
                KafkaUpdateMessage message = new KafkaUpdateMessage(mediaId, previousStatus, media.getStatus());
                try { this.kafka.send("mediastatistics", message.toString()); } 
                catch (Exception e) { LOGGER.info("Kafka offline."); }
                return fwlMediaRepository.save(media);
            }
        }
        return null;
    }

    public void deleteMedia(String listId, Integer mediaId) {
        FwlMedia media = fwlMediaRepository.findByListIdAndMediaId(listId, mediaId);
        if (media != null) {
            KafkaUpdateMessage message = new KafkaUpdateMessage(mediaId, media.getStatus(), null);
            try { this.kafka.send("mediastatistics", message.toString()); } 
            catch (Exception e) { LOGGER.info("Kafka offline."); }
            fwlMediaRepository.delete(media);
        }
    }
}
