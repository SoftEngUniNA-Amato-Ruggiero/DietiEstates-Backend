package it.softengunina.userservice.controller;

import it.softengunina.userservice.dto.UserAgencyRoleDTO;
import it.softengunina.userservice.model.User;
import it.softengunina.userservice.repository.UserRepository;
import it.softengunina.userservice.services.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository<User> userRepository;
    private final TokenService tokenService;

    UserController(UserRepository<User> userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
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

    @GetMapping("/role")
    public UserAgencyRoleDTO getRole() {
        User user = userRepository.findByCognitoSub(tokenService.getCognitoSub())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return new UserAgencyRoleDTO(user, user.getAgency(), user.getRole());
    }
}
