package it.softengunina.dietiestatesbackend.controller.userscontroller;

import it.softengunina.dietiestatesbackend.dto.usersdto.UserDTO;
import it.softengunina.dietiestatesbackend.model.users.User;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for managing user-related operations.
 * Provides endpoints to retrieve user information by username or ID.
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final BaseUserRepository userRepository;

    UserController(BaseUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves user information by username.
     *
     * @param username The username of the user to retrieve.
     * @return UserDTO containing user details.
     * @throws ResponseStatusException if the user is not found.
     */
    @GetMapping
    public UserDTO getUserByUsername(@RequestParam String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new UserDTO(user);
    }

    /**
     * Retrieves user information by user ID.
     *
     * @param id The ID of the user to retrieve.
     * @return UserDTO containing user details.
     * @throws ResponseStatusException if the user is not found.
     */
    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new UserDTO(user);
    }
}
