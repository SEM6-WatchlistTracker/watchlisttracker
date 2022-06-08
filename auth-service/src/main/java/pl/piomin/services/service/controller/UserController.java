package pl.piomin.services.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.FormParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.piomin.services.service.model.NewUser;
import pl.piomin.services.service.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@FormParam("email") String email, @FormParam("password") String password) {
        LOGGER.info("Signing in user " + email);
        String token = authService.signIn(email, password);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/signup") // add body/param with displayname
    public ResponseEntity<NewUser> signUp(@FormParam("email") String email, @FormParam("password") String password) {
        LOGGER.info("Signing up user " + email);
        NewUser createdUser = authService.signUp(email, password);
        return ResponseEntity.ok(createdUser);
    }
}
