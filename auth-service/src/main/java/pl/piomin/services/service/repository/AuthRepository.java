package pl.piomin.services.service.repository;

import pl.piomin.services.service.model.document.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends MongoRepository<User, String> {
    public User findByEmail(String email);
}
