package it.softengunina.dietiestatesbackend.controller.userscontroller;

import it.softengunina.dietiestatesbackend.dto.usersdto.UserWithAgencyDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserDTO;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
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
 * Controller for managing real estate manager-related operations.
 */
@Slf4j
@RestController
@RequestMapping("/managers")
public class RealEstateManagerController {
    private final RealEstateAgentRepository agentRepository;
    private final RealEstateManagerRepository managerRepository;
    private final TokenService tokenService;

    RealEstateManagerController(RealEstateAgentRepository agentRepository,
                                RealEstateManagerRepository managerRepository,
                                TokenService tokenService) {
        this.agentRepository = agentRepository;
        this.managerRepository = managerRepository;
        this.tokenService = tokenService;
    }

    /**
     * Promotes an existing agent to a manager for the requesting manager's agency.
     * Access is restricted to users with a manager role.
     * @param req The UserDTO containing the username of the agent to be promoted.
     * @return The UserWithAgencyDTO for the newly created manager's data along with agency information.
     * @throws ResponseStatusException with HttpStatus.FORBIDDEN if the requesting user is not a manager.
     * @throws ResponseStatusException with HttpStatus.NOT_FOUND if the requested agent is not found.
     * @throws ResponseStatusException with HttpStatus.CONFLICT if the requested agent is already a manager.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public UserWithAgencyDTO createManager(@Valid @RequestBody UserDTO req) {
        try {
            RealEstateManager manager = managerRepository.findByBusinessUser_User_CognitoSub(tokenService.getCognitoSub())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a manager"));

            RealEstateAgent agent = agentRepository.findByBusinessUser_AgencyAndBusinessUser_User_Username(manager.getAgency(), req.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agent not found"));

            if (managerRepository.existsByBusinessUser_User_Username(agent.getUsername())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already a manager");
            }

            RealEstateManager promoted = managerRepository.save(new RealEstateManager(agent.getBusinessUser()));
            return new UserWithAgencyDTO(promoted);

        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already a manager");
        }
    }
}
