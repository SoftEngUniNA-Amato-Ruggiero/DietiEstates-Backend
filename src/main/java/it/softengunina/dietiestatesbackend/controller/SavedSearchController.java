package it.softengunina.dietiestatesbackend.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionSearchResultDTO;
import it.softengunina.dietiestatesbackend.dto.searchdto.SearchRequestDTO;
import it.softengunina.dietiestatesbackend.dto.searchdto.SearchRequestForRentDTO;
import it.softengunina.dietiestatesbackend.dto.searchdto.SearchRequestForSaleDTO;
import it.softengunina.dietiestatesbackend.model.savedsearches.SavedSearch;
import it.softengunina.dietiestatesbackend.model.savedsearches.SavedSearchForRent;
import it.softengunina.dietiestatesbackend.model.savedsearches.SavedSearchForSale;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.savedsearchesrepository.SavedSearchForRentRepository;
import it.softengunina.dietiestatesbackend.repository.savedsearchesrepository.SavedSearchForSaleRepository;
import it.softengunina.dietiestatesbackend.repository.savedsearchesrepository.SavedSearchRepository;
import it.softengunina.dietiestatesbackend.visitor.savedsearchvisitor.SavedSearchVisitorImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/saved-searches")
public class SavedSearchController {
    public static final String SAVED_SEARCH_NOT_FOUND = "Saved search not found.";

    private final SavedSearchRepository<SavedSearch> savedSearchRepository;
    private final SavedSearchForSaleRepository savedSearchForSaleRepository;
    private final SavedSearchForRentRepository savedSearchForRentRepository;
    private final SavedSearchVisitorImpl savedSearchVisitorImpl;

    public SavedSearchController(SavedSearchRepository<SavedSearch> savedSearchRepository,
                                 SavedSearchForSaleRepository savedSearchForSaleRepository,
                                 SavedSearchForRentRepository savedSearchForRentRepository,
                                 SavedSearchVisitorImpl savedSearchVisitorImpl) {
        this.savedSearchRepository = savedSearchRepository;
        this.savedSearchForSaleRepository = savedSearchForSaleRepository;
        this.savedSearchForRentRepository = savedSearchForRentRepository;
        this.savedSearchVisitorImpl = savedSearchVisitorImpl;
    }

    /**
     * Retrieves all saved searches for the authenticated user.
     * If the user is not authenticated, it throws a 401 UNAUTHORIZED error.
     * @param pageable Pagination information
     * @return Iterable of SavedSearch objects
     * @throws ResponseStatusException if the user is not authenticated
     */
    @GetMapping
    public Page<SavedSearch> getAllSavedSearches(Pageable pageable,
                                                 @RequestAttribute(name = "user", required = true) BaseUser user) {
        return savedSearchRepository.findByUser(user, pageable);
    }

    /**
     * Retrieves a saved search by its ID for the authenticated user.
     * If the user is not authenticated, it throws a 401 UNAUTHORIZED error.
     * If the saved search is not found, it throws a 404 NOT FOUND error.
     * @param id The ID of the saved search
     * @return The SavedSearch object
     * @throws ResponseStatusException if the user is not authenticated or if the saved search is not found
     */
    @GetMapping("/{id}")
    public SavedSearch getSavedSearchById(@PathVariable Long id,
                                          @RequestAttribute(name = "user", required = true) BaseUser user) {
        return savedSearchRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, SAVED_SEARCH_NOT_FOUND));
    }

    /**
     * Deletes a saved search by its ID for the authenticated user.
     * Returns a 204 NO CONTENT status on successful deletion.
     * If the user is not authenticated, it throws a 401 UNAUTHORIZED error.
     * If the saved search is not found, it throws a 404 NOT FOUND error.
     * @param id The ID of the saved search
     * @throws ResponseStatusException if the user is not authenticated or if the saved search is not found
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSavedSearchById(@PathVariable Long id,
                                      @RequestAttribute(name = "user", required = true) BaseUser user) {
        SavedSearch savedSearch = savedSearchRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, SAVED_SEARCH_NOT_FOUND));
        savedSearchRepository.delete(savedSearch);
    }

    /**
     * Executes a saved search by its ID for the authenticated user and returns the search results.
     * If the user is not authenticated, it throws a 401 UNAUTHORIZED error.
     * If the saved search is not found, it throws a 404 NOT FOUND error.
     * @param id The ID of the saved search
     * @param pageable Pagination information
     * @return Page of InsertionSearchResultDTO objects
     * @throws ResponseStatusException if the user is not authenticated or if the saved search is not found
     */
    @GetMapping("/{id}/execute")
    public Page<InsertionSearchResultDTO> executeSavedSearchById(@PathVariable Long id,
                                                                 Pageable pageable,
                                                                 @RequestAttribute(name = "user", required = true) BaseUser user) {
        SavedSearch savedSearch = savedSearchRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, SAVED_SEARCH_NOT_FOUND));
        return savedSearch.getResults(savedSearchVisitorImpl, pageable);
    }

    /**
     * Creates a new saved search for the authenticated user.
     * If the user is not authenticated, it throws a 401 UNAUTHORIZED error.
     * @param searchReq The search request data
     * @return The created SavedSearch object
     * @throws ResponseStatusException if the user is not authenticated
     */
    @PostMapping
    public SavedSearch createSavedSearch(@RequestBody SearchRequestDTO searchReq,
                                         @RequestAttribute(name = "user", required = true) BaseUser user) {
        return savedSearchRepository.save(searchReq.toSavedSearch(user));
    }

    /**
     * Creates a new saved search for properties for sale for the authenticated user.
     * If the user is not authenticated, it throws a 401 UNAUTHORIZED error.
     * @param searchReq The search request data for properties for sale
     * @return The created SavedSearchForSale object
     * @throws ResponseStatusException if the user is not authenticated
     */
    @PostMapping("/for-sale")
    public SavedSearchForSale createSavedSearchForSale(@RequestBody SearchRequestForSaleDTO searchReq,
                                                       @RequestAttribute(name = "user", required = true) BaseUser user) {
        return savedSearchForSaleRepository.save(searchReq.toSavedSearch(user));
    }

    /**
     * Creates a new saved search for properties for rent for the authenticated user.
     * If the user is not authenticated, it throws a 401 UNAUTHORIZED error.
     * @param searchReq The search request data for properties for rent
     * @return The created SavedSearchForRent object
     * @throws ResponseStatusException if the user is not authenticated
     */
    @PostMapping("/for-rent")
    public SavedSearchForRent createSavedSearchForRent(@RequestBody SearchRequestForRentDTO searchReq,
                                                       @RequestAttribute(name = "user", required = true) BaseUser user) {
        return savedSearchForRentRepository.save(searchReq.toSavedSearch(user));
    }
}
