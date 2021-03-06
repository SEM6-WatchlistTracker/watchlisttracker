package pl.piomin.services.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import pl.piomin.services.service.model.document.User;
import pl.piomin.services.service.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired private UserService userService;

    @GetMapping("/get/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        LOGGER.info("Getting user " + userId);
        User foundUser = userService.getUser(userId);
        if (foundUser != null) return new ResponseEntity<>(foundUser, HttpStatus.OK);
        else return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User newUser,
                @RequestHeader(value = "role") String requesterRole) {
        if (requesterRole.equals("ADMIN")) {
            User createdUser = userService.createUser(newUser);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        }
        else return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<User> updateUser(@RequestBody User newUser, @PathVariable String userId,
                @RequestHeader(value = "userId") String requesterId) {
        if (requesterId.equals(userId)) {
            User updatedUser = userService.updateUser(newUser, userId);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        else return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId,
                @RequestHeader(value = "role") String requesterRole) {
        if (requesterRole.equals("ADMIN")) {
            userService.deleteUser(userId);
            return new ResponseEntity<>("User deleted.", HttpStatus.OK);
        }
        else return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }
}
