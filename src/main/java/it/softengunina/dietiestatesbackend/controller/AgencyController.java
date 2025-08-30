package it.softengunina.dietiestatesbackend.controller;

import it.softengunina.dietiestatesbackend.dto.RealEstateAgencyDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserWithAgencyDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserDTO;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.repository.RealEstateAgencyRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateAgentRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateManagerRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/agencies")
public class AgencyController {
    private final RealEstateAgencyRepository agencyRepository;
    private final BaseUserRepository userRepository;
    private final RealEstateAgentRepository agentRepository;
    private final RealEstateManagerRepository managerRepository;
    private final TokenService tokenService;

    AgencyController(RealEstateAgencyRepository agencyRepository,
                     BaseUserRepository userRepository,
                     RealEstateAgentRepository agentRepository,
                     RealEstateManagerRepository managerRepository,
                     TokenService tokenService) {
        this.agencyRepository = agencyRepository;
        this.userRepository = userRepository;
        this.agentRepository = agentRepository;
        this.managerRepository = managerRepository;
        this.tokenService = tokenService;
    }

    @GetMapping
    public Page<RealEstateAgencyDTO> getAgencies(Pageable pageable) {
        Page<RealEstateAgency> agencies = agencyRepository.findAll(pageable);
        return agencies.map(RealEstateAgencyDTO::new);
    }

    @GetMapping("/{id}")
    public RealEstateAgencyDTO getAgencyById(@PathVariable Long id) {
        RealEstateAgency agency = agencyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new RealEstateAgencyDTO(agency);
    }

    @GetMapping("/{id}/agents")
    public Page<UserDTO> getAgentsByAgencyId(@PathVariable Long id, Pageable pageable) {
        RealEstateAgency agency = agencyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agency not found"));
        Page<RealEstateAgent> agents = agentRepository.findByAgency(agency, pageable);
        return agents.map(UserDTO::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public UserWithAgencyDTO createAgency(@Valid @RequestBody RealEstateAgencyDTO req) {
        BaseUser user = findUserNotAffiliatedWithAnAgency(tokenService.getCognitoSub());

        RealEstateAgency agency = agencyRepository.saveAndFlush(new RealEstateAgency(req.getName(), req.getIban()));
        RealEstateManager manager = managerRepository.save(new RealEstateManager(user, agency));

        return new UserWithAgencyDTO(manager);
    }

    private BaseUser findUserNotAffiliatedWithAnAgency(String cognitoSub) {
        if (agentRepository.findByUser_CognitoSub(cognitoSub).isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are already affiliated with an agency");
        }

        return userRepository.findByCognitoSub(cognitoSub)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "We could not find you in our database"));
    }
}
