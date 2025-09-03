package it.softengunina.dietiestatesbackend.controller.userscontroller;

import it.softengunina.dietiestatesbackend.dto.usersdto.UserWithAgencyDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserDTO;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateAgentRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateManagerRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for managing real estate agent-related operations.
 * Provides an endpoint to create a new agent for the requesting manager's agency.
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
     * Promotes an existing user to a real estate agent for the requesting manager's agency.
     * Access is restricted to users with a manager role.
     * @param req The UserDTO containing the username of the user to be promoted.
     * @return The UserWithAgencyDTO for the newly created agent's data along with agency information.
     * @throws ResponseStatusException with HttpStatus.FORBIDDEN if the requesting user is not a manager.
     * @throws ResponseStatusException with HttpStatus.NOT_FOUND if the requested user is not found.
     * @throws ResponseStatusException with HttpStatus.CONFLICT if the requested user is already an agent.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public UserWithAgencyDTO createAgent(@Valid @RequestBody UserDTO req) {
        try {
            RealEstateManager manager = managerRepository.findByUser_CognitoSub(tokenService.getCognitoSub())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a manager"));

            BaseUser user = userRepository.findByUsername(req.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            RealEstateAgent agent = agentRepository.save(new RealEstateAgent(user, manager.getAgency()));
            return new UserWithAgencyDTO(agent);

        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already an agent");
        }
    }
}
