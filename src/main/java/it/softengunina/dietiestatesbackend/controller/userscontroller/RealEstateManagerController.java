package it.softengunina.dietiestatesbackend.controller.userscontroller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.softengunina.dietiestatesbackend.dto.usersdto.BusinessUserResponseDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserRequestDTO;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateManagerRepository;
import it.softengunina.dietiestatesbackend.services.UserPromotionService;
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
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/managers")
public class RealEstateManagerController {
    private final BaseUserRepository userRepository;
    private final RealEstateManagerRepository managerRepository;
    private final UserPromotionService userPromotionService;

    RealEstateManagerController(BaseUserRepository userRepository,
                                RealEstateManagerRepository managerRepository,
                                UserPromotionService userPromotionService) {
        this.userRepository = userRepository;
        this.managerRepository = managerRepository;
        this.userPromotionService = userPromotionService;
    }

    /**
     * Promotes an existing user to a manager for the requesting manager's agency.
     * Access is restricted to users with a manager role.
     * @param req The UserDTO containing the username of the user to be promoted.
     * @return The UserWithAgencyDTO for the newly created manager's data along with agency information.
     * @throws ResponseStatusException with HttpStatus.FORBIDDEN if the requesting user is not a manager.
     * @throws ResponseStatusException with HttpStatus.NOT_FOUND if the requested user is not found.
     * @throws ResponseStatusException with HttpStatus.CONFLICT if the requested user is already a manager.
     * @throws ResponseStatusException with HttpStatus.CONFLICT if the requested user works for a different agency.
     * @throws ResponseStatusException with HttpStatus.INTERNAL_SERVER_ERROR for other errors.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public BusinessUserResponseDTO createManager(@RequestAttribute(name="manager") RealEstateManager manager,
                                                 @Valid @RequestBody UserRequestDTO req) {
        try {
            BaseUser user = userRepository.findByUsername(req.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            if (user.hasRoleByName(RealEstateManager.class.getSimpleName())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already a manager");
            }

            BusinessUser businessUser = userPromotionService.promoteToBusinessUser(user, manager.getAgency());

            RealEstateManager promoted = managerRepository.save(new RealEstateManager(businessUser));
            return new BusinessUserResponseDTO(promoted);

        } catch (ResponseStatusException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Data integrity violation: " + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error: " + e.getMessage());
        }
    }
}
