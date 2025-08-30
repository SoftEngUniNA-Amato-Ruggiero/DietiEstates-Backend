package it.softengunina.dietiestatesbackend.controller.userscontroller;

import it.softengunina.dietiestatesbackend.dto.usersdto.UserWithAgencyDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserDTO;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateManagerRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/managers")
public class RealEstateManagerController {
    private final BaseUserRepository userRepository;
    private final RealEstateManagerRepository managerRepository;
    private final TokenService tokenService;

    RealEstateManagerController(BaseUserRepository userRepository,
                                RealEstateManagerRepository managerRepository,
                                TokenService tokenService) {
        this.userRepository = userRepository;
        this.managerRepository = managerRepository;
        this.tokenService = tokenService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public UserWithAgencyDTO createManager(@Valid @RequestBody UserDTO req) {
        RealEstateManager manager = managerRepository.findByUser_CognitoSub(tokenService.getCognitoSub())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a manager"));

        BaseUser user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        RealEstateManager promoted = managerRepository.save(new RealEstateManager(user, manager.getAgency()));
        return new UserWithAgencyDTO(promoted);
    }
}
