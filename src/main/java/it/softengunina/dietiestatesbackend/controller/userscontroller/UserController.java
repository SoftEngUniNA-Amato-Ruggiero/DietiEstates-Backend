package it.softengunina.dietiestatesbackend.controller.userscontroller;

import it.softengunina.dietiestatesbackend.dto.usersdto.UserAgencyRoleDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserDTO;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.User;
import it.softengunina.dietiestatesbackend.repository.usersrepository.UserRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository<BaseUser> userRepository;
    private final TokenService tokenService;

    UserController(UserRepository<BaseUser> userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @GetMapping
    public UserDTO getUserByUsername(@RequestParam String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new UserDTO(user);
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new UserDTO(user);
    }

    @GetMapping("/role")
    public UserAgencyRoleDTO getRole() {
        User user = userRepository.findByCognitoSub(tokenService.getCognitoSub())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return new UserAgencyRoleDTO(user);
    }
}
