package it.softengunina.dietiestatesbackend.controller;

import it.softengunina.dietiestatesbackend.dto.RealEstateAgencyDTO;
import it.softengunina.dietiestatesbackend.dto.UserAgencyRoleDTO;
import it.softengunina.dietiestatesbackend.dto.UserDTO;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.model.users.User;
import it.softengunina.dietiestatesbackend.repository.RealEstateAgencyRepository;
import it.softengunina.dietiestatesbackend.repository.RealEstateAgentRepository;
import it.softengunina.dietiestatesbackend.repository.UserRepository;
import it.softengunina.dietiestatesbackend.services.UserPromotionService;
import it.softengunina.dietiestatesbackend.services.TokenService;
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
    private final UserRepository<User> userRepository;
    private final RealEstateAgentRepository<RealEstateAgent> agentRepository;
    private final TokenService tokenService;
    private final UserPromotionService promotionService;

    AgencyController(RealEstateAgencyRepository agencyRepository,
                     UserRepository<User> userRepository,
                     RealEstateAgentRepository<RealEstateAgent> agentRepository,
                     TokenService tokenService, UserPromotionService promotionService) {
        this.agencyRepository = agencyRepository;
        this.userRepository = userRepository;
        this.agentRepository = agentRepository;
        this.tokenService = tokenService;
        this.promotionService = promotionService;
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
    public UserAgencyRoleDTO createAgency(@RequestBody RealEstateAgencyDTO req) {
        User user = userRepository.findByCognitoSub(tokenService.getCognitoSub())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        try {
            RealEstateAgency agency = agencyRepository.saveAndFlush(new RealEstateAgency(req.getIban(), req.getName()));
            RealEstateManager manager = promotionService.promoteToManager(user, agency);
            return new UserAgencyRoleDTO(manager);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Agency or manager not created");
        }
    }
}
