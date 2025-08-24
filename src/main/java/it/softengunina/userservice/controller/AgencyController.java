package it.softengunina.userservice.controller;

import it.softengunina.userservice.dto.AgencyAndManagerDTO;
import it.softengunina.userservice.dto.RealEstateAgencyDTO;
import it.softengunina.userservice.model.RealEstateAgency;
import it.softengunina.userservice.model.RealEstateManager;
import it.softengunina.userservice.model.User;
import it.softengunina.userservice.repository.RealEstateAgencyRepository;
import it.softengunina.userservice.repository.UserRepository;
import it.softengunina.userservice.services.PromotionService;
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
    private final RealEstateAgencyRepository agencyRepository;
    private final UserRepository<User> userRepository;
    private final TokenService tokenService;
    private final PromotionService promotionService;

    AgencyController(RealEstateAgencyRepository agencyRepository,
                     UserRepository<User> userRepository,
                     TokenService tokenService, PromotionService promotionService) {
        this.agencyRepository = agencyRepository;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.promotionService = promotionService;
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
    public AgencyAndManagerDTO createAgency(@RequestBody RealEstateAgencyDTO req) {
        String cognitoSub = tokenService.getCognitoSub();
        User user = userRepository.findByCognitoSub(cognitoSub)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        promotionService.verifyUserIsNotAnAgent(user);
        RealEstateAgency agency = agencyRepository.saveAndFlush(new RealEstateAgency(req.getIban(), req.getName()));

        try {
            RealEstateManager manager = promotionService.promoteUserToManager(user, agency);
            return new AgencyAndManagerDTO(agency, manager);

        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Manager not created");
        }
    }
}
