package pl.piomin.services.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.FormParam;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import pl.piomin.services.service.model.NewUser;
import pl.piomin.services.service.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    @Autowired private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@FormParam("email") String email, @FormParam("password") String password) {
        LOGGER.info("Signing in user " + email);
        String token = authService.signIn(email, password);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/signup")
    public ResponseEntity<NewUser> signUp(@FormParam("email") String email, @FormParam("password") String password, @FormParam("displayName") String displayName) {
        LOGGER.info("Signing up user " + email);
        NewUser createdUser = authService.signUp(email, password, displayName);
        if (createdUser != null) return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        else return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @DeleteMapping("/deleteaccount/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId,
                @RequestHeader(value = "userId") String requesterId) {
        if (requesterId.equals(userId)) {
            LOGGER.info("Deleting user " + userId);
            boolean deleted = authService.deleteUser(userId);
            if (deleted) return new ResponseEntity<>("User deleted.", HttpStatus.OK);
            else return new ResponseEntity<>("Could not delete user.", HttpStatus.SERVICE_UNAVAILABLE);
        }
        else return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }
}
