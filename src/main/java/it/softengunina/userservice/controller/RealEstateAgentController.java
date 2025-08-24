package it.softengunina.userservice.controller;

import it.softengunina.userservice.dto.UserDTO;
import it.softengunina.userservice.model.RealEstateAgent;
import it.softengunina.userservice.model.RealEstateManager;
import it.softengunina.userservice.model.User;
import it.softengunina.userservice.repository.RealEstateAgentRepository;
import it.softengunina.userservice.repository.RealEstateManagerRepository;
import it.softengunina.userservice.repository.UserRepository;
import it.softengunina.userservice.services.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/agents")
public class RealEstateAgentController {
    UserRepository<User> userRepository;
    RealEstateAgentRepository<RealEstateAgent> agentRepository;
    RealEstateManagerRepository managerRepository;
    TokenService tokenService;

    RealEstateAgentController(UserRepository<User> userRepository,
                              RealEstateAgentRepository<RealEstateAgent> agentRepository,
                              RealEstateManagerRepository managerRepository,
                              TokenService tokenService) {
        this.userRepository = userRepository;
        this.agentRepository = agentRepository;
        this.managerRepository = managerRepository;
        this.tokenService = tokenService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public RealEstateAgent createAgent(@RequestBody UserDTO req) {
        RealEstateManager manager = managerRepository.findByCognitoSub(tokenService.getCognitoSub())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Only managers can create agents"));

        if (agentRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already an agent");
        }

        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        agentRepository.insertAgent(user.getId(), manager.getAgency().getId());
        agentRepository.flush();

        return agentRepository.findById(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Agent not found after creation"));
    }
}
