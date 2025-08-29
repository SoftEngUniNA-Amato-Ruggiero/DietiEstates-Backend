package it.softengunina.dietiestatesbackend.controller.userscontroller;

import it.softengunina.dietiestatesbackend.dto.usersdto.UserWithAgencyDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserDTO;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.UserWithAgency;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateManagerRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import it.softengunina.dietiestatesbackend.strategy.UserPromotionStrategyImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
    private final BaseUserRepository<BaseUser> userRepository;
    private final RealEstateManagerRepository managerRepository;
    private final TokenService tokenService;
    private final UserPromotionStrategyImpl promotionService;

    RealEstateManagerController(BaseUserRepository<BaseUser> userRepository,
                                RealEstateManagerRepository managerRepository,
                                TokenService tokenService,
                                UserPromotionStrategyImpl promotionService) {
        this.userRepository = userRepository;
        this.managerRepository = managerRepository;
        this.tokenService = tokenService;
        this.promotionService = promotionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public UserWithAgencyDTO createManager(@Valid @RequestBody UserDTO req) {
        RealEstateManager manager = managerRepository.findByCognitoSub(tokenService.getCognitoSub())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a manager"));

        BaseUser user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        try {
            UserWithAgency promotedManager = user.getPromotionToManagerFunction(manager.getAgency()).apply(promotionService);
            return new UserWithAgencyDTO(promotedManager);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
