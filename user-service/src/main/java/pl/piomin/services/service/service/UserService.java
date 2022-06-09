package pl.piomin.services.service.service;

import pl.piomin.services.service.model.document.User;
import pl.piomin.services.service.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired private UserRepository userRepository;

    public User getUser(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User createUser(User newUser) {
        User user = new User(newUser.getDisplayName());
        return userRepository.save(user);
    }

    public User updateUser(User updatedUser, String userId) {
        User user = userRepository.findById(userId).orElse(null);
        user.setDisplayName(updatedUser.getDisplayName());
        return userRepository.save(user);
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}
