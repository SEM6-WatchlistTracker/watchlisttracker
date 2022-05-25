package pl.piomin.services.service.repository;

import java.util.List;

import pl.piomin.services.service.model.document.FwlMedia;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FwlMediaRepository extends MongoRepository<FwlMedia, String> {
    public List<FwlMedia> findByListId(String listId);
    public FwlMedia findByListIdAndMediaId(String listId, Integer mediaId);
    public void deleteAllByListId(String listId);
}