package it.softengunina.dietiestatesbackend.controller.userscontroller;

import it.softengunina.dietiestatesbackend.dto.usersdto.BusinessUserResponseDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserRequestDTO;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BusinessUserRepository;
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
    private final BusinessUserRepository businessUserRepository;
    private final RealEstateAgentRepository agentRepository;
    private final RealEstateManagerRepository managerRepository;
    private final TokenService tokenService;

    RealEstateAgentController(BaseUserRepository userRepository,
                              BusinessUserRepository businessUserRepository,
                              RealEstateAgentRepository agentRepository,
                              RealEstateManagerRepository managerRepository,
                              TokenService tokenService) {
        this.userRepository = userRepository;
        this.businessUserRepository = businessUserRepository;
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
     * @throws ResponseStatusException with HttpStatus.INTERNAL_SERVER_ERROR for other errors.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public BusinessUserResponseDTO createAgent(@Valid @RequestBody UserRequestDTO req) {
        try {
            RealEstateManager manager = managerRepository.findByBusinessUser_User_CognitoSub(tokenService.getCognitoSub())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a manager"));

            BaseUser user = userRepository.findByUsername(req.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            if (user.hasRole(RealEstateAgent.getRole())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already an agent");
            }

            BusinessUser businessUser = businessUserRepository.findById(user.getId())
                    .orElse(new BusinessUser(user, manager.getAgency()));

            RealEstateAgent agent = agentRepository.save(new RealEstateAgent(businessUser));
            return new BusinessUserResponseDTO(agent);

        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Data integrity violation: " + e.getMessage());
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error: " + e.getMessage());
        }
    }
}
