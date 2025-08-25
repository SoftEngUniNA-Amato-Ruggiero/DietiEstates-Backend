package it.softengunina.userservice.controller;

import it.softengunina.userservice.model.RealEstateAgent;
import it.softengunina.userservice.model.insertions.Address;
import it.softengunina.userservice.model.insertions.Insertion;
import it.softengunina.userservice.model.insertions.InsertionForSale;
import it.softengunina.userservice.repository.InsertionRepository;
import it.softengunina.userservice.repository.RealEstateAgentRepository;
import it.softengunina.userservice.services.TokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/insertions")
public class InsertionController {
    private final InsertionRepository insertionRepository;
    private final RealEstateAgentRepository<RealEstateAgent> agentRepository;
    private final TokenService tokenService;

    public InsertionController(InsertionRepository insertionRepository, RealEstateAgentRepository<RealEstateAgent> agentRepository, TokenService tokenService) {
        this.insertionRepository = insertionRepository;
        this.agentRepository = agentRepository;
        this.tokenService = tokenService;
    }

    @PostMapping
    public Insertion createInsertion(@RequestBody Address address) {
        RealEstateAgent uploader = agentRepository.findByCognitoSub(tokenService.getCognitoSub())
                .orElseThrow(() -> new RuntimeException("RealEstateAgent not found"));

        return insertionRepository.save(new InsertionForSale(address, null, uploader, 20.9));
    }
}
