package it.softengunina.userservice.controller;

import it.softengunina.userservice.dto.UserDTO;
import it.softengunina.userservice.model.RealEstateAgent;
import it.softengunina.userservice.model.RealEstateManager;
import it.softengunina.userservice.model.User;
import it.softengunina.userservice.repository.RealEstateManagerRepository;
import it.softengunina.userservice.repository.UserRepository;
import it.softengunina.userservice.services.PromotionService;
import it.softengunina.userservice.services.TokenService;
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
    private final PromotionService promotionService;

    RealEstateManagerController(UserRepository<User> userRepository,
                                RealEstateManagerRepository managerRepository,
                                TokenService tokenService,
                                PromotionService promotionService) {
        this.userRepository = userRepository;
        this.managerRepository = managerRepository;
        this.tokenService = tokenService;
        this.promotionService = promotionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public RealEstateManager createManager(@RequestBody UserDTO req) {
        RealEstateManager manager = managerRepository.findByCognitoSub(tokenService.getCognitoSub())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Only managers can promote users to managers"));

        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user instanceof RealEstateManager u) {
            if (u.getAgency().equals(manager.getAgency())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already a manager of your agency");
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User belongs to another agency");
            }
        }

        if (user instanceof RealEstateAgent agent) {
            if (agent.getAgency().equals(manager.getAgency())) {
                return promotionService.promoteAgentToManager(agent);
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User belongs to another agency");
            }
        }

        try {
            return promotionService.promoteUserToManager(user, manager.getAgency());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
