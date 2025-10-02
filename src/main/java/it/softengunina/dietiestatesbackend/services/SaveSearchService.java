package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.dto.searchdto.SearchRequestDTO;
import it.softengunina.dietiestatesbackend.exceptions.AuthenticationNotFoundException;
import it.softengunina.dietiestatesbackend.exceptions.JwtNotFoundException;
import it.softengunina.dietiestatesbackend.model.SavedSearch;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.Set;

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
            saveSearch(searchReq, user);
        } catch (JwtNotFoundException e) {
            // User not authenticated, do not save the search
        }
    }

    private SavedSearch saveSearch(SearchRequestDTO searchReq, BaseUser user) {
        Point point = searchReq.getPoint();
        Set<String> tagsSet = searchReq.getTagsSet();
        Double distance = searchReq.getDistance();

        SavedSearch savedSearch = SavedSearch.builder()
                .user(user)
                .geometry(point)
                .distance(distance)
                .tags(tagsSet)
                .build();

        log.info("Search: {}", savedSearch);
        return savedSearch;
    }
}
