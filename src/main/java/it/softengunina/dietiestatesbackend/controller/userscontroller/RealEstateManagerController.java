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
     * Promotes an existing agent to a manager within the same agency.
     * Access is restricted to users with a manager role.
     * @param req The user DTO containing the username of the agent to be promoted.
     * @return The promoted manager's DTO including agency information.
     * @throws ResponseStatusException if the requester is not a manager, if the agent is not found,
     *                                 if the agent is affiliated with another agency that is not the requesting manager's,
     *                                 or if the agent is already a manager.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public UserWithAgencyDTO createManager(@Valid @RequestBody UserDTO req) {

        try {
            RealEstateManager manager = managerRepository.findByUser_CognitoSub(tokenService.getCognitoSub())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a manager"));

            RealEstateAgent agent = agentRepository.findByUser_Username(req.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not an agent"));

            if(!agent.getAgency().equals(manager.getAgency())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not affiliated with your agency");
            }

            RealEstateManager promoted = managerRepository.save(new RealEstateManager(agent));
            return new UserWithAgencyDTO(promoted);

        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Agent is already a manager for your agency");
        }
    }
}
