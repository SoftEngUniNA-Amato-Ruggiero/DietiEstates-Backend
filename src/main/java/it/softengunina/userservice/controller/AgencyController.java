package it.softengunina.userservice.controller;

import it.softengunina.userservice.dto.RealEstateAgencyDTO;
import it.softengunina.userservice.model.RealEstateAgency;
import it.softengunina.userservice.model.RealEstateAgent;
import it.softengunina.userservice.model.RealEstateManager;
import it.softengunina.userservice.model.User;
import it.softengunina.userservice.repository.RealEstateAgencyRepository;
import it.softengunina.userservice.repository.RealEstateAgentRepository;
import it.softengunina.userservice.repository.RealEstateManagerRepository;
import it.softengunina.userservice.repository.UserRepository;
import it.softengunina.userservice.services.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/agencies")
public class AgencyController {

    RealEstateAgencyRepository agencyRepository;
    UserRepository<User> userRepository;
    RealEstateAgentRepository<RealEstateAgent> agentRepository;
    RealEstateManagerRepository managerRepository;
    TokenService tokenService;

    AgencyController(RealEstateAgencyRepository agencyRepository,
                     UserRepository<User> userRepository,
                     RealEstateAgentRepository<RealEstateAgent> agentRepository,
                     RealEstateManagerRepository managerRepository,
                     TokenService tokenService) {
        this.agencyRepository = agencyRepository;
        this.userRepository = userRepository;
        this.agentRepository = agentRepository;
        this.managerRepository = managerRepository;
        this.tokenService = tokenService;
    }

    @GetMapping
    public Page<RealEstateAgency> getAgencies(Pageable pageable) {
        return agencyRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public RealEstateAgency getAgencyById(@PathVariable Long id) {
        return agencyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public Pair<RealEstateAgency, RealEstateManager> createAgency(@RequestBody RealEstateAgencyDTO req) {
        String cognitoSub = tokenService.getCognitoSub();

        User user = userRepository.findByCognitoSub(cognitoSub)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        if (agentRepository.findById(user.getId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already an agent");
        }

        RealEstateAgency agency = agencyRepository.saveAndFlush(new RealEstateAgency(req.getIban(), req.getName()));

        agentRepository.insertAgent(user.getId(), agency.getId());
        agentRepository.flush();

        managerRepository.insertManager(user.getId());
        managerRepository.flush();

        RealEstateManager manager = managerRepository.findById(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Manager not found after creation"));
        return Pair.of(agency, manager);
    }
}
