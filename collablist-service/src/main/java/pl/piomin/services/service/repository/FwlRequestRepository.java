package pl.piomin.services.service.repository;

import java.util.List;

import pl.piomin.services.service.model.document.FwlRequest;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FwlRequestRepository extends MongoRepository<FwlRequest, String> {
    public List<FwlRequest> findByMemberId(String memberId);
    public Integer countByMemberId(String memberId);
}