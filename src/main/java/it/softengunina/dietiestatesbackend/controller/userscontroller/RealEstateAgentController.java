package it.softengunina.dietiestatesbackend.controller.userscontroller;

import it.softengunina.dietiestatesbackend.dto.usersdto.UserWithAgencyDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserDTO;
import it.softengunina.dietiestatesbackend.exceptions.UserIsAlreadyAffiliatedWithAgencyException;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateAgentRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateManagerRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import it.softengunina.dietiestatesbackend.services.UserNotAffiliatedWithAgencyService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
    private final RealEstateAgentRepository agentRepository;
    private final RealEstateManagerRepository managerRepository;
    private final UserNotAffiliatedWithAgencyService userNotAffiliatedWithAgencyService;
    private final TokenService tokenService;

    RealEstateAgentController(RealEstateAgentRepository agentRepository,
                              RealEstateManagerRepository managerRepository,
                              UserNotAffiliatedWithAgencyService userNotAffiliatedWithAgencyService,
                              TokenService tokenService) {
        this.agentRepository = agentRepository;
        this.managerRepository = managerRepository;
        this.userNotAffiliatedWithAgencyService = userNotAffiliatedWithAgencyService;
        this.tokenService = tokenService;
    }

    /**
     * Creates a new real estate agent for the manager's agency.
     * Access is restricted to users with a manager role.
     * @param req The UserDTO containing the username of the new agent.
     * @return The UserWithAgencyDTO for the newly created agent's data along with agency information.
     * @throws ResponseStatusException with status 403 if the requesting user is not a manager.
     * @throws ResponseStatusException with status 400 if the requested user is already affiliated with an agency.
     * @throws ResponseStatusException with status 404 if the requested user is not found.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public UserWithAgencyDTO createAgent(@Valid @RequestBody UserDTO req) {
        try {
            RealEstateManager manager = managerRepository.findByUser_CognitoSub(tokenService.getCognitoSub())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a manager"));

            BaseUser user = userNotAffiliatedWithAgencyService.findByUsername(req.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            RealEstateAgent agent = agentRepository.save(new RealEstateAgent(user, manager.getAgency()));
            return new UserWithAgencyDTO(agent);

        } catch (UserIsAlreadyAffiliatedWithAgencyException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user is already affiliated with an agency");
        }
    }
}
