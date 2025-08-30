package it.softengunina.dietiestatesbackend.controller.insertionscontroller;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionDTO;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionWithPriceDTO;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionForSale;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.repository.insertionsrepository.InsertionForSaleRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateAgentRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/insertions/for-sale")
public class InsertionForSaleController {
    private final InsertionForSaleRepository insertionForSaleRepository;
    private final RealEstateAgentRepository agentRepository;
    private final TokenService tokenService;

    public InsertionForSaleController(InsertionForSaleRepository insertionForSaleRepository,
                                      RealEstateAgentRepository agentRepository,
                                      TokenService tokenService) {
        this.insertionForSaleRepository = insertionForSaleRepository;
        this.agentRepository = agentRepository;
        this.tokenService = tokenService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InsertionDTO createInsertion(@Valid @RequestBody InsertionWithPriceDTO req) {
        RealEstateAgent uploader = agentRepository.findByUser_CognitoSub(tokenService.getCognitoSub())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not an agent"));

        InsertionForSale insertion = insertionForSaleRepository.save(new InsertionForSale(req.getAddress(), req.getDetails(), uploader, req.getPrice()));
        return insertion.getDTOFactory().build();
    }
}
