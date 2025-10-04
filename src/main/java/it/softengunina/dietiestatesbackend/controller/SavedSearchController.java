package it.softengunina.dietiestatesbackend.controller;

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
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/saved-searches")
public class SavedSearchController {
    private static final String AUTHENTICATE_TO_SEE_YOUR_SAVED_SEARCHES = "Please authenticate to see your saved searches.";
    public static final String AUTHENTICATE_TO_SAVE_YOUR_SEARCHES = "Please authenticate to save your searches.";

    private final SavedSearchRepository<SavedSearch> savedSearchRepository;
    private final SavedSearchForSaleRepository savedSearchForSaleRepository;
    private final SavedSearchForRentRepository savedSearchForRentRepository;
    private final BaseUserRepository userRepository;
    private final TokenService tokenService;

    public SavedSearchController(SavedSearchRepository<SavedSearch> savedSearchRepository,
                                 SavedSearchForSaleRepository savedSearchForSaleRepository,
                                 SavedSearchForRentRepository savedSearchForRentRepository,
                                 BaseUserRepository userRepository,
                                 TokenService tokenService) {
        this.savedSearchRepository = savedSearchRepository;
        this.savedSearchForSaleRepository = savedSearchForSaleRepository;
        this.savedSearchForRentRepository = savedSearchForRentRepository;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    /**
     * Retrieves all saved searches for the authenticated user.
     * If the user is not authenticated, it throws a 401 UNAUTHORIZED error.
     * @param pageable Pagination information
     * @return Iterable of SavedSearch objects
     * @throws ResponseStatusException if the user is not authenticated
     */
    @GetMapping
    public Page<SavedSearch> getAllSavedSearches(Pageable pageable) {
        BaseUser user = userRepository.findByCognitoSub(tokenService.getCognitoSub())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, AUTHENTICATE_TO_SEE_YOUR_SAVED_SEARCHES));
        return savedSearchRepository.findByUser(user, pageable);
    }

    /**
     * Creates a new saved search for the authenticated user.
     * If the user is not authenticated, it throws a 401 UNAUTHORIZED error.
     * @param searchReq The search request data
     * @return The created SavedSearch object
     * @throws ResponseStatusException if the user is not authenticated
     */
    @PostMapping
    public SavedSearch createSavedSearch(@RequestBody SearchRequestDTO searchReq) {
        BaseUser user = userRepository.findByCognitoSub(tokenService.getCognitoSub())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, AUTHENTICATE_TO_SAVE_YOUR_SEARCHES));
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
    public SavedSearchForSale createSavedSearchForSale(@RequestBody SearchRequestForSaleDTO searchReq) {
        BaseUser user = userRepository.findByCognitoSub(tokenService.getCognitoSub())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, AUTHENTICATE_TO_SAVE_YOUR_SEARCHES));
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
    public SavedSearchForRent createSavedSearchForRent(@RequestBody SearchRequestForRentDTO searchReq) {
        BaseUser user = userRepository.findByCognitoSub(tokenService.getCognitoSub())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, AUTHENTICATE_TO_SAVE_YOUR_SEARCHES));
        return savedSearchForRentRepository.save(searchReq.toSavedSearch(user));
    }
}
