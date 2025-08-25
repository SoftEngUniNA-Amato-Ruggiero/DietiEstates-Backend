package it.softengunina.dietiestatesbackend.controller;

import it.softengunina.dietiestatesbackend.dto.UserAgencyRoleDTO;
import it.softengunina.dietiestatesbackend.dto.UserDTO;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.model.users.User;
import it.softengunina.dietiestatesbackend.repository.RealEstateManagerRepository;
import it.softengunina.dietiestatesbackend.repository.UserRepository;
import it.softengunina.dietiestatesbackend.services.UserPromotionService;
import it.softengunina.dietiestatesbackend.services.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/agents")
public class RealEstateAgentController {
    private final UserRepository<User> userRepository;
    private final RealEstateManagerRepository managerRepository;
    private final TokenService tokenService;
    private final UserPromotionService promotionService;

    RealEstateAgentController(UserRepository<User> userRepository,
                              RealEstateManagerRepository managerRepository,
                              TokenService tokenService, UserPromotionService promotionService) {
        this.userRepository = userRepository;
        this.managerRepository = managerRepository;
        this.tokenService = tokenService;
        this.promotionService = promotionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public UserAgencyRoleDTO createAgent(@RequestBody UserDTO req) {
        RealEstateManager manager = managerRepository.findByCognitoSub(tokenService.getCognitoSub())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a manager"));

        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        try {
            RealEstateAgent agent = promotionService.promoteToAgent(user, manager.getAgency());
            return new UserAgencyRoleDTO(agent);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
