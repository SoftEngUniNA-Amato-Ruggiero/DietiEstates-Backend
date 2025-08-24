package it.softengunina.userservice.controller;

import it.softengunina.userservice.model.User;
import it.softengunina.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository<User> userRepository;

    UserController(UserRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public User getUserByUsername(@RequestParam String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
