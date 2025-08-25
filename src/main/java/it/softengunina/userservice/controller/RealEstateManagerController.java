package it.softengunina.userservice.controller;

import it.softengunina.userservice.dto.UserDTO;
import it.softengunina.userservice.model.users.RealEstateManager;
import it.softengunina.userservice.model.users.User;
import it.softengunina.userservice.repository.RealEstateManagerRepository;
import it.softengunina.userservice.repository.UserRepository;
import it.softengunina.userservice.services.UserPromotionService;
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
    private final UserPromotionService promotionService;

    RealEstateManagerController(UserRepository<User> userRepository,
                                RealEstateManagerRepository managerRepository,
                                TokenService tokenService,
                                UserPromotionService promotionService) {
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a manager"));

        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        try {
            return promotionService.promoteToManager(user, manager.getAgency());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
