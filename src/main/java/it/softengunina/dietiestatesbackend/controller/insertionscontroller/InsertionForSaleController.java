package it.softengunina.dietiestatesbackend.controller.insertionscontroller;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.requestdto.InsertionForSaleRequestDTO;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionResponseDTO;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionWithPriceResponseDTO;
import it.softengunina.dietiestatesbackend.dto.searchdto.SearchRequestForSaleDTO;
import it.softengunina.dietiestatesbackend.factory.insertionfactory.InsertionForSaleFactory;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionForSale;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import it.softengunina.dietiestatesbackend.repository.insertionsrepository.InsertionForSaleRepository;
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
 * Controller for handling requests related to insertions for sale.
 */
@RestController
@RequestMapping("/insertions/for-sale")
public class InsertionForSaleController {
    private final InsertionForSaleRepository insertionForSaleRepository;
    private final BusinessUserRepository businessUserRepository;
    private final InsertionDTOVisitorImpl insertionDTOVisitor;
    private final InsertionForSaleFactory insertionForSaleFactory;
    private final TokenService tokenService;

    public InsertionForSaleController(InsertionForSaleRepository insertionForSaleRepository,
                                      BusinessUserRepository businessUserRepository,
                                      InsertionDTOVisitorImpl insertionDTOVisitor,
                                      InsertionForSaleFactory insertionForSaleFactory,
                                      TokenService tokenService) {
        this.insertionForSaleRepository = insertionForSaleRepository;
        this.businessUserRepository = businessUserRepository;
        this.insertionDTOVisitor = insertionDTOVisitor;
        this.insertionForSaleFactory = insertionForSaleFactory;
        this.tokenService = tokenService;
    }

    /**
     * Creates a new insertion for sale.
     *
     * @param req The insertion data.
     * @return The created insertion DTO.
     * @throws ResponseStatusException if the user is not a business user.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InsertionWithPriceResponseDTO createInsertionForSale(@Valid @RequestBody InsertionForSaleRequestDTO req) {
        BusinessUser uploader = businessUserRepository.findByUser_CognitoSub(tokenService.getCognitoSub())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot create an insertion."));

        InsertionForSale insertion = insertionForSaleRepository.save(
                insertionForSaleFactory.createInsertion(req, uploader)
        );
        return insertion.accept(insertionDTOVisitor);
    }

    /**
     * Searches for insertions for sale based on the provided search criteria.
     *
     * @param searchReq The search criteria.
     * @param pageable  Pagination information.
     * @return A page of insertion DTOs matching the search criteria.
     */
    @GetMapping("/search")
    public Page<InsertionResponseDTO> searchInsertionsForSale(@ModelAttribute SearchRequestForSaleDTO searchReq,
                                                              Pageable pageable) {

        Set<String> tagsSet = searchReq.getTagsSet();
        return insertionForSaleRepository.search(searchReq, tagsSet, tagsSet.size(), pageable)
                .map(i -> i.accept(insertionDTOVisitor));
    }
}
