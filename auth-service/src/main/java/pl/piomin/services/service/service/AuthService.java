package pl.piomin.services.service.service;

import pl.piomin.services.service.model.NewUser;
import pl.piomin.services.service.model.Role;
import pl.piomin.services.service.model.document.AuthUser;
import pl.piomin.services.service.model.User;
import pl.piomin.services.service.repository.AuthRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
    @Autowired private AuthRepository authRepository;
    @Autowired private TokenService tokenService;
    private final String adminToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJpc3MiOiJBdXRoZW50aWNhdGlvblNlcnZpY2UiLCJ1c2VySWQiOiI2MmExZDFmMjdiYWM4MDVjOTQ2MGUxYjAiLCJpYXQiOjE2NTQ3NzIyMTB9._r467xpOhBuIZt3afJ62aUjRB5UYC_QVpQugiHS-1Cc";

    public String signIn(String email, String password) {
        AuthUser foundUser = authRepository.findByEmail(email);
        if (MatchPassword(password, foundUser.getPassword())) {
            return createToken(foundUser);
        }
        return null;
    }

    public String createToken(AuthUser user) {
        try {
            String token = tokenService.createToken(user);
            if(!token.isEmpty()) return token;
        }
        catch(Exception e) {}
        return null;
    }

    public String EncodePassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        return encodedPassword;
    }

    public boolean MatchPassword(String loginPassword, String userPassword){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(loginPassword, userPassword);
    }

    public NewUser signUp(String email, String password, String displayName) {
        // realistically would check email validity and if exists first.
        User createdUser = null;
        try {
            User userToCreate = new User(displayName);
            WebClient client = WebClient.create("https://watchlisttracker-polishedstudios.cloud.okteto.net");
            Mono<User> response = client.post()
                .uri("/users/create")
                .header("Authorization", adminToken)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(Mono.just(userToCreate), User.class)
                .accept(org.springframework.http.MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(User.class);
            createdUser = response.block();
            LOGGER.info("Created user in User service.");
        } catch (Exception e) { LOGGER.info("Could not reach user service.");}

        if (createdUser != null) {
            String encodedPassword = EncodePassword(password);
            AuthUser createdAuthUser = authRepository.save(new AuthUser(createdUser.getUserId(), email, encodedPassword, Role.USER));
            String token = createToken(createdAuthUser);
            NewUser newUser = new NewUser(createdAuthUser.getUserId(), token);
            return newUser;
        }
        else return null;
    }

    public boolean deleteUser(String userId) {
        boolean isOnline = false;
        try {
            WebClient client = WebClient.create("https://watchlisttracker-polishedstudios.cloud.okteto.net");
            Mono<Void> response = client.delete()
                .uri("/users/delete/" + userId)
                .header("Authorization", adminToken)
                .retrieve()
                .bodyToMono(Void.class);
            response.block();
            LOGGER.info("Deleted user in User service.");
            isOnline = true;
        } catch (Exception e) { LOGGER.info("Could not reach user service.");}
        if (isOnline) authRepository.deleteById(userId);
        return isOnline;
        // realistically would also delete their lists and media but will not be covered.
    }
}
