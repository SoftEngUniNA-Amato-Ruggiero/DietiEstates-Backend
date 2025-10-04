package it.softengunina.dietiestatesbackend.controller.insertionscontroller;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.requestdto.InsertionForRentRequestDTO;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionSearchResultDTO;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionWithRentResponseDTO;
import it.softengunina.dietiestatesbackend.dto.searchdto.SearchRequestForRentDTO;
import it.softengunina.dietiestatesbackend.factory.insertionfactory.InsertionForRentFactory;
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

import java.util.Set;

/**
 * Controller for handling requests related to insertions for rent.
 */
@RestController
@RequestMapping("/insertions/for-rent")
public class InsertionForRentController {
    private final InsertionForRentRepository insertionForRentRepository;
    private final BusinessUserRepository businessUserRepository;
    private final InsertionDTOVisitorImpl insertionDTOVisitor;
    private final InsertionForRentFactory insertionForRentFactory;
    private final TokenService tokenService;

    public InsertionForRentController(InsertionForRentRepository insertionForRentRepository,
                                      BusinessUserRepository businessUserRepository,
                                      InsertionDTOVisitorImpl insertionDTOVisitor,
                                      InsertionForRentFactory insertionForRentFactory,
                                      TokenService tokenService) {
        this.insertionForRentRepository = insertionForRentRepository;
        this.businessUserRepository = businessUserRepository;
        this.insertionDTOVisitor = insertionDTOVisitor;
        this.insertionForRentFactory = insertionForRentFactory;
        this.tokenService = tokenService;
    }

    /**
     * Creates a new insertion for rent.
     *
     * @param req The insertion data.
     * @return The created insertion DTO.
     * @throws ResponseStatusException if the user is not a business user.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InsertionWithRentResponseDTO createInsertionForRent(@Valid @RequestBody InsertionForRentRequestDTO req) {
        BusinessUser uploader = businessUserRepository.findByUser_CognitoSub(tokenService.getCognitoSub())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Only business users can create insertions."));

        InsertionForRent insertion = insertionForRentRepository.save(
                insertionForRentFactory.createInsertion(req, uploader)
        );
        return insertion.accept(insertionDTOVisitor);
    }
    /**
     * Searches for insertions for rent based on the provided search criteria.
     *
     * @param searchReq The search criteria.
     * @param pageable  Pagination information.
     * @return A page of insertion DTOs matching the search criteria.
     */
    @GetMapping("/search")
    public Page<InsertionSearchResultDTO> searchInsertionsForRent(@ModelAttribute SearchRequestForRentDTO searchReq,
                                                                  Pageable pageable) {

        Set<String> tagsSet = searchReq.getTagsSet();
        return insertionForRentRepository.search(searchReq, tagsSet, tagsSet.size(), pageable);
    }
}