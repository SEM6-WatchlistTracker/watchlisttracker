package pl.piomin.services.organization.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import pl.piomin.services.organization.model.Organization;

public interface OrganizationRepository extends MongoRepository<Organization, String> {
	
}