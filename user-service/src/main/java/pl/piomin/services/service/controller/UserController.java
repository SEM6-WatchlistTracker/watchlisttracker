package pl.piomin.services.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.piomin.services.service.model.document.User;
import pl.piomin.services.service.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        LOGGER.info("Getting user " + userId);
        User foundUser = userService.getUser(userId);
        return ResponseEntity.ok(foundUser);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User newUser) {
        LOGGER.info("Creating user " + newUser.getDisplayName());
        User createdUser = userService.createUser(newUser);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<User> updateUser(@RequestBody User newUser, @PathVariable String userId) {
        LOGGER.info("Updating user " + userId);
        User updatedUser = userService.updateUser(newUser, userId);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        LOGGER.info("Deleting user " + userId);
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted.");
    }
}
