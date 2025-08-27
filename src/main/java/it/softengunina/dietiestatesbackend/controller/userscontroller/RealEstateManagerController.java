package it.softengunina.dietiestatesbackend.controller.userscontroller;

import it.softengunina.dietiestatesbackend.dto.usersdto.UserAgencyRoleDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserDTO;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.model.users.User;
import it.softengunina.dietiestatesbackend.model.users.UserWithAgency;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateManagerRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.UserRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import it.softengunina.dietiestatesbackend.services.PromotionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/managers")
public class RealEstateManagerController {
    private final UserRepository<User> userRepository;
    private final RealEstateManagerRepository managerRepository;
    private final TokenService tokenService;
    private final PromotionServiceImpl promotionService;

    RealEstateManagerController(UserRepository<User> userRepository,
                                RealEstateManagerRepository managerRepository,
                                TokenService tokenService,
                                PromotionServiceImpl promotionService) {
        this.userRepository = userRepository;
        this.managerRepository = managerRepository;
        this.tokenService = tokenService;
        this.promotionService = promotionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public UserAgencyRoleDTO createManager(@RequestBody UserDTO req) {
        RealEstateManager manager = managerRepository.findByCognitoSub(tokenService.getCognitoSub())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a manager"));

        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        try {
            UserWithAgency promotedManager = user.getPromotionToManagerFunction(promotionService).apply(manager.getAgency());
            return new UserAgencyRoleDTO(promotedManager);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
