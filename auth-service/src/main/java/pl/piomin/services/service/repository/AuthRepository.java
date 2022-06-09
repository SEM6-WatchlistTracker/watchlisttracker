package pl.piomin.services.service.repository;

import pl.piomin.services.service.model.document.AuthUser;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends MongoRepository<AuthUser, String> {
    public AuthUser findByEmail(String email);
}
