package it.softengunina.dietiestatesbackend.controller;

import it.softengunina.dietiestatesbackend.dto.searchdto.SearchRequestDTO;
import it.softengunina.dietiestatesbackend.model.savedsearches.SavedSearch;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
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
    private final SavedSearchRepository<SavedSearch> savedSearchRepository;
    private final BaseUserRepository userRepository;
    private final TokenService tokenService;

    public SavedSearchController(SavedSearchRepository<SavedSearch> savedSearchRepository,
                                 BaseUserRepository userRepository,
                                 TokenService tokenService) {
        this.savedSearchRepository = savedSearchRepository;
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please authenticate to see your saved searches."));
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please authenticate to save your searches."));
        return savedSearchRepository.save(searchReq.toSavedSearch(user));
    }
}
