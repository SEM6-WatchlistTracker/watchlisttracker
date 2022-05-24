package pl.piomin.services.service.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import pl.piomin.services.service.model.Organization;

public interface OrganizationRepository extends MongoRepository<Organization, String> {
	
}