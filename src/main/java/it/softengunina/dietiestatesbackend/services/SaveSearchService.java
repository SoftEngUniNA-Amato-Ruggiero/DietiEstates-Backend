package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.dto.searchdto.SearchRequestDTO;
import it.softengunina.dietiestatesbackend.exceptions.AuthenticationNotFoundException;
import it.softengunina.dietiestatesbackend.exceptions.JwtNotFoundException;
import it.softengunina.dietiestatesbackend.model.savedsearches.SavedSearch;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SaveSearchService {
    BaseUserRepository userRepository;
    TokenService tokenService;

    public SaveSearchService(BaseUserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public void saveSearchForAuthenticatedUser(SearchRequestDTO searchReq) {
        try {
            BaseUser user = userRepository.findByCognitoSub(tokenService.getCognitoSub())
                    .orElseThrow(() -> new AuthenticationNotFoundException("User not found"));

            SavedSearch savedSearch = searchReq.toSavedSearch(user);
            log.info("Search: {}", savedSearch);
        } catch (JwtNotFoundException e) {
            // User not authenticated, do not save the search
        }
    }
}
