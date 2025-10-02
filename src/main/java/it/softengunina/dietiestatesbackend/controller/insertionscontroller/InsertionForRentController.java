package it.softengunina.dietiestatesbackend.controller.insertionscontroller;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.requestdto.InsertionForRentRequestDTO;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionResponseDTO;
import it.softengunina.dietiestatesbackend.dto.searchdto.SearchRequestForRentDTO;
import it.softengunina.dietiestatesbackend.factory.insertionfactory.InsertionForRentFactory;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionForRent;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import it.softengunina.dietiestatesbackend.repository.insertionsrepository.InsertionForRentRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BusinessUserRepository;
import it.softengunina.dietiestatesbackend.services.SaveSearchService;
import it.softengunina.dietiestatesbackend.services.TokenService;
import it.softengunina.dietiestatesbackend.visitor.insertionsdtovisitor.InsertionDTOVisitorImpl;
import jakarta.validation.Valid;
import org.locationtech.jts.geom.Point;
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
    private final SaveSearchService saveSearchService;

    public InsertionForRentController(InsertionForRentRepository insertionForRentRepository,
                                      BusinessUserRepository businessUserRepository,
                                      InsertionDTOVisitorImpl insertionDTOVisitor,
                                      InsertionForRentFactory insertionForRentFactory,
                                      TokenService tokenService,
                                      SaveSearchService saveSearchService) {
        this.insertionForRentRepository = insertionForRentRepository;
        this.businessUserRepository = businessUserRepository;
        this.insertionDTOVisitor = insertionDTOVisitor;
        this.insertionForRentFactory = insertionForRentFactory;
        this.tokenService = tokenService;
        this.saveSearchService = saveSearchService;
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
    public InsertionResponseDTO createInsertionForRent(@Valid @RequestBody InsertionForRentRequestDTO req) {
        BusinessUser uploader = businessUserRepository.findByUser_CognitoSub(tokenService.getCognitoSub())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Only business users can create insertions."));

        InsertionForRent insertion = insertionForRentRepository.save(
                insertionForRentFactory.createInsertion(req, uploader)
        );
        return insertion.accept(insertionDTOVisitor);
    }
    /**
     * Searches for insertions for rent based on the provided search criteria.
     * Also saves the search criteria for the authenticated user.
     *
     * @param searchReq The search criteria.
     * @param pageable  Pagination information.
     * @return A page of insertion DTOs matching the search criteria.
     */
    @GetMapping("/search")
    public Page<InsertionResponseDTO> searchInsertionsForRent(@ModelAttribute SearchRequestForRentDTO searchReq,
                                                              Pageable pageable) {

        saveSearchService.saveSearchForAuthenticatedUser(searchReq);
        return getInsertionResponseDTOS(searchReq, pageable);
    }

    private Page<InsertionResponseDTO> getInsertionResponseDTOS(SearchRequestForRentDTO searchReq, Pageable pageable) {
        Point point = searchReq.getPoint();
        Set<String> tagsSet = searchReq.getTagsSet();

        if (tagsSet.isEmpty()) {
            return searchInsertionsWithoutTags(searchReq, pageable, point);
        } else {
            return searchInsertionsWithTags(searchReq, pageable, point, tagsSet);
        }
    }

    private Page<InsertionResponseDTO> searchInsertionsWithTags(SearchRequestForRentDTO searchReq, Pageable pageable, Point point, Set<String> tagsSet) {
        return insertionForRentRepository.searchWithTags(
                point,
                searchReq.getDistance(),
                tagsSet,
                tagsSet.size(),
                searchReq.getMinSize(),
                searchReq.getMinNumberOfRooms(),
                searchReq.getMaxFloor(),
                searchReq.getHasElevator(),
                pageable
        ).map(i -> i.accept(insertionDTOVisitor));
    }

    private Page<InsertionResponseDTO> searchInsertionsWithoutTags(SearchRequestForRentDTO searchReq, Pageable pageable, Point point) {
        return insertionForRentRepository.searchWithoutTags(
                point,
                searchReq.getDistance(),
                searchReq.getMinSize(),
                searchReq.getMinNumberOfRooms(),
                searchReq.getMaxFloor(),
                searchReq.getHasElevator(),
                pageable
        ).map(i -> i.accept(insertionDTOVisitor));
    }
}