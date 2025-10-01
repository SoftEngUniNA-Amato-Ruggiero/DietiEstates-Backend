package it.softengunina.dietiestatesbackend.controller.insertionscontroller;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionResponseDTO;
import it.softengunina.dietiestatesbackend.exceptions.AuthenticationNotFoundException;
import it.softengunina.dietiestatesbackend.model.insertions.BaseInsertion;
import it.softengunina.dietiestatesbackend.model.SavedSearch;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.insertionsrepository.BaseInsertionRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import it.softengunina.dietiestatesbackend.visitor.insertionsdtovisitor.InsertionDTOVisitorImpl;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

/**
 * Controller for handling requests related to all kinds of insertions.
 */
@RestController
@RequestMapping("/insertions")
@Slf4j
public class InsertionController {
    private final TokenService tokenService;
    private final BaseUserRepository userRepository;
    private final BaseInsertionRepository<BaseInsertion> insertionRepository;
    private final InsertionDTOVisitorImpl visitor;

    public InsertionController(TokenService tokenService, BaseUserRepository userRepository, BaseInsertionRepository<BaseInsertion> insertionRepository, InsertionDTOVisitorImpl visitor) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.insertionRepository = insertionRepository;
        this.visitor = visitor;
    }

    /**
     * Retrieves a paginated list of insertions near a specified location within a given distance.
     *
     * @param lat the latitude of the location.
     * @param lng the longitude of the location.
     * @param distance the distance in degrees.
     * @param pageable pagination information.
     * @return A page of insertion DTOs.
     */
    @GetMapping
    public Page<InsertionResponseDTO> getInsertionsByLocation(@RequestParam double lat, @RequestParam double lng, @RequestParam double distance, Pageable pageable) {
        Point point = new GeometryFactory().createPoint(new Coordinate(lng, lat));
        point.setSRID(4326);
        return insertionRepository.findByLocationNear(point, distance, pageable).map(i -> i.accept(visitor));
    }

    @GetMapping("/search")
    public Page<InsertionResponseDTO> searchInsertionsByLocationAndTag(@RequestParam Double lat,
                                                                       @RequestParam Double lng,
                                                                       @RequestParam Double distance,
                                                                       @RequestParam(required = false) Integer minSize,
                                                                       @RequestParam(required = false) Integer minNumberOfRooms,
                                                                       @RequestParam(required = false) Integer maxFloor,
                                                                       @RequestParam(required = false) Boolean hasElevator,
                                                                       @RequestParam(required = false) String tags,
                                                                       Pageable pageable) {

        Point point = new GeometryFactory().createPoint(new Coordinate(lng, lat));
        point.setSRID(4326);

        Set<String> tagsSet = (tags == null || tags.isEmpty()) ? Set.of() : Set.of(tags.split(","));

        try {
            BaseUser user = userRepository.findByCognitoSub(tokenService.getCognitoSub())
                    .orElseThrow(() -> new AuthenticationNotFoundException("User not found"));

            SavedSearch savedSearch = SavedSearch.builder()
                    .user(user)
                    .geometry(point)
                    .distance(distance)
                    .tags(tagsSet)
                    .build();

            log.info("Search: {}", savedSearch);
        } catch (UsernameNotFoundException e) {
            // User not authenticated, proceed without saving the search
        }

        if (tagsSet.isEmpty()) {
            return insertionRepository.search_without_tags(
                    point,
                    distance,
                    minSize,
                    minNumberOfRooms,
                    maxFloor,
                    hasElevator,
                    pageable
            ).map(i -> i.accept(visitor));
        } else {
            return insertionRepository.search_with_tags(
                    point,
                    distance,
                    tagsSet,
                    tagsSet.size(),
                    minSize,
                    minNumberOfRooms,
                    maxFloor,
                    hasElevator,
                    pageable
            ).map(i -> i.accept(visitor));
        }
    }

    /**
     * Retrieves a specific insertion by its ID.
     *
     * @param id The ID of the insertion to retrieve.
     * @return The insertion DTO.
     * @throws ResponseStatusException if the insertion is not found.
     */
    @GetMapping("/{id}")
    public InsertionResponseDTO getInsertionById(@PathVariable Long id) {
        BaseInsertion insertion = insertionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Insertion not found"));
        return insertion.accept(visitor);
    }
}
