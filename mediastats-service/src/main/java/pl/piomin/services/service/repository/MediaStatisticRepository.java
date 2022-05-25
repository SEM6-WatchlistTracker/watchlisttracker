package pl.piomin.services.service.repository;

import pl.piomin.services.service.model.document.MediaStatistic;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaStatisticRepository extends MongoRepository<MediaStatistic, Integer> {}
