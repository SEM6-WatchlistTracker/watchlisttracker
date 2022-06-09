package pl.piomin.services.service.service;

import pl.piomin.services.service.model.document.AuthUser;
import pl.piomin.services.service.repository.AuthRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired private AuthRepository userRepository;

    public AuthUser getUser(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    // public AuthUser createUser(AuthUser newUser) {
    //     AuthUser user = new AuthUser(newUser.getDisplayName());
    //     return userRepository.save(user);
    // }

    // public AuthUser updateUser(AuthUser updatedUser, String userId) {
    //     AuthUser user = userRepository.findById(userId).orElse(null);
    //     user.setDisplayName(updatedUser.getDisplayName());
    //     return userRepository.save(user);
    // }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}
