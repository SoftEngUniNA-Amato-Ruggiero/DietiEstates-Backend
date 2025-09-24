package it.softengunina.dietiestatesbackend.controller.insertionscontroller;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionDTO;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionWithPriceDTO;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionForRent;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import it.softengunina.dietiestatesbackend.repository.insertionsrepository.InsertionForRentRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BusinessUserRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import it.softengunina.dietiestatesbackend.visitor.insertionsdtovisitor.InsertionDTOVisitorImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/insertions/for-rent")
public class InsertionForRentController {
    private final InsertionForRentRepository insertionForRentRepository;
    private final BusinessUserRepository businessUserRepository;
    private final InsertionDTOVisitorImpl insertionDTOVisitor;
    private final TokenService tokenService;

    public InsertionForRentController(InsertionForRentRepository insertionForRentRepository,
                                      BusinessUserRepository businessUserRepository,
                                      InsertionDTOVisitorImpl insertionDTOVisitor,
                                      TokenService tokenService) {
        this.insertionForRentRepository = insertionForRentRepository;
        this.businessUserRepository = businessUserRepository;
        this.insertionDTOVisitor = insertionDTOVisitor;
        this.tokenService = tokenService;
    }

    @GetMapping
    public Page<InsertionDTO> getInsertions(Pageable pageable) {
        return insertionForRentRepository.findAll(pageable).map(i -> i.accept(insertionDTOVisitor));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InsertionDTO createInsertion(@Valid @RequestBody InsertionWithPriceDTO req) {
        BusinessUser uploader = businessUserRepository.findByUser_CognitoSub(tokenService.getCognitoSub())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Only business users can create insertions."));

        InsertionForRent insertion = insertionForRentRepository.save(new InsertionForRent(req.getAddress(), req.getDetails(), uploader.getUser(), uploader.getAgency(), req.getPrice()));
        return insertion.accept(insertionDTOVisitor);
    }
}