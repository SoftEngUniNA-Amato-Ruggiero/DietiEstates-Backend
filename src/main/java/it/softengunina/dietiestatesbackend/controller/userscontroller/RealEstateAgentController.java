package it.softengunina.dietiestatesbackend.controller.userscontroller;

import it.softengunina.dietiestatesbackend.dto.usersdto.UserWithAgencyDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserDTO;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateAgentRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateManagerRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for managing real estate agent-related operations.
 */
@Slf4j
@RestController
@RequestMapping("/agents")
public class RealEstateAgentController {
    private final BaseUserRepository userRepository;
    private final RealEstateAgentRepository agentRepository;
    private final RealEstateManagerRepository managerRepository;
    private final TokenService tokenService;

    RealEstateAgentController(BaseUserRepository userRepository,
                              RealEstateAgentRepository agentRepository,
                              RealEstateManagerRepository managerRepository,
                              TokenService tokenService) {
        this.userRepository = userRepository;
        this.agentRepository = agentRepository;
        this.managerRepository = managerRepository;
        this.tokenService = tokenService;
    }

    /**
     * Creates a new real estate agent for the manager's agency.
     * Access is restricted to users with a manager role.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public UserWithAgencyDTO createAgent(@Valid @RequestBody UserDTO req) {
        RealEstateManager manager = managerRepository.findByUser_CognitoSub(tokenService.getCognitoSub())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a manager"));

        BaseUser user = findUserNotAffiliatedWithAnAgency(req.getUsername());

        RealEstateAgent agent = agentRepository.save(new RealEstateAgent(user, manager.getAgency()));
        return new UserWithAgencyDTO(agent);
    }

    private BaseUser findUserNotAffiliatedWithAnAgency(String username) {
        if (agentRepository.findByUser_Username(username).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user is already affiliated with an agency");
        }

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}
