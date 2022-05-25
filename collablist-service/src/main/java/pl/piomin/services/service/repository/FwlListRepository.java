package pl.piomin.services.service.repository;

import java.util.List;

import pl.piomin.services.service.model.document.FwlList;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FwlListRepository extends MongoRepository<FwlList, String> {
    public List<FwlList> findAllByOwnerIdOrMemberId(String ownerId, String memberId);
}