package it.softengunina.dietiestatesbackend.controller.userscontroller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.softengunina.dietiestatesbackend.dto.usersdto.BusinessUserResponseDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserRequestDTO;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateAgentRepository;
import it.softengunina.dietiestatesbackend.services.UserPromotionService;
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
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/agents")
public class RealEstateAgentController {
    private final BaseUserRepository userRepository;
    private final RealEstateAgentRepository agentRepository;
    private final UserPromotionService userPromotionService;

    RealEstateAgentController(BaseUserRepository userRepository,
                              RealEstateAgentRepository agentRepository,
                              UserPromotionService userPromotionService) {
        this.userRepository = userRepository;
        this.agentRepository = agentRepository;
        this.userPromotionService = userPromotionService;
    }

    /**
     * Promotes an existing user to a real estate agent for the requesting manager's agency.
     * Access is restricted to users with a manager role.
     * @param req The UserDTO containing the username of the user to be promoted.
     * @return The UserWithAgencyDTO for the newly created agent's data along with agency information.
     * @throws ResponseStatusException with HttpStatus.FORBIDDEN if the requesting user is not a manager or
     * @throws ResponseStatusException with HttpStatus.NOT_FOUND if the requested user is not found.
     * @throws ResponseStatusException with HttpStatus.CONFLICT if the requested user is already an agent.
     * @throws ResponseStatusException with HttpStatus.CONFLICT if the requested user works for a different agency.
     * @throws ResponseStatusException with HttpStatus.INTERNAL_SERVER_ERROR for other errors.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public BusinessUserResponseDTO createAgent(@RequestAttribute(name="manager") RealEstateManager manager,
                                               @Valid @RequestBody UserRequestDTO req) {
        try {
            BaseUser user = userRepository.findByUsername(req.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            if (user.hasRoleByName(RealEstateAgent.class.getSimpleName())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already an agent");
            }

            BusinessUser businessUser = userPromotionService.promoteToBusinessUser(user, manager.getAgency());

            RealEstateAgent agent = agentRepository.save(new RealEstateAgent(businessUser));
            return new BusinessUserResponseDTO(agent);

        } catch (ResponseStatusException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Data integrity violation: " + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error: " + e.getMessage());
        }
    }

}
