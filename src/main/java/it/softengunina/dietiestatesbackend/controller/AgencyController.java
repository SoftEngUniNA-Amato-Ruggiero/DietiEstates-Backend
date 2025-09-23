package it.softengunina.dietiestatesbackend.controller;

import it.softengunina.dietiestatesbackend.dto.RealEstateAgencyDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserWithAgencyDTO;
import it.softengunina.dietiestatesbackend.exceptions.UserIsAlreadyAffiliatedWithAgencyException;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.repository.RealEstateAgencyRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateAgentRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateManagerRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import it.softengunina.dietiestatesbackend.services.UserNotAffiliatedWithAgencyService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for managing real estate agencies and their agents.
 * Provides endpoints to create, retrieve, and list agencies and to list their associated agents.
 */
@Slf4j
@RestController
@RequestMapping("/agencies")
public class AgencyController {
    private final RealEstateAgencyRepository agencyRepository;
    private final RealEstateAgentRepository agentRepository;
    private final RealEstateManagerRepository managerRepository;
    private final UserNotAffiliatedWithAgencyService userNotAffiliatedWithAgencyService;
    private final TokenService tokenService;

    AgencyController(RealEstateAgencyRepository agencyRepository,
                     RealEstateAgentRepository agentRepository,
                     RealEstateManagerRepository managerRepository,
                     UserNotAffiliatedWithAgencyService userNotAffiliatedWithAgencyService,
                     TokenService tokenService) {
        this.agencyRepository = agencyRepository;
        this.agentRepository = agentRepository;
        this.managerRepository = managerRepository;
        this.userNotAffiliatedWithAgencyService = userNotAffiliatedWithAgencyService;
        this.tokenService = tokenService;
    }

    /**
     * Retrieves a paginated list of all real estate agencies.
     * @param pageable Pagination information.
     * @return A paginated list of RealEstateAgencyDTOs representing the agencies.
     */
    @GetMapping
    public Page<RealEstateAgencyDTO> getAgencies(Pageable pageable) {
        Page<RealEstateAgency> agencies = agencyRepository.findAll(pageable);
        return agencies.map(RealEstateAgencyDTO::new);
    }

    /**
     * Retrieves a specific real estate agency by its ID.
     * @param id The ID of the agency to retrieve.
     * @return The RealEstateAgencyDTO representing the agency.
     * @throws ResponseStatusException with HttpStatus.NOT_FOUND if the agency does not exist.
     */
    @GetMapping("/{id}")
    public RealEstateAgencyDTO getAgencyById(@PathVariable Long id) {
        RealEstateAgency agency = agencyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new RealEstateAgencyDTO(agency);
    }

    /**
     * Retrieves a paginated list of agents associated with a specific real estate agency.
     * @param id The ID of the agency whose agents are to be retrieved.
     * @param pageable Pagination information.
     * @return A paginated list of UserDTOs representing the agents.
     * @throws ResponseStatusException with HttpStatus.NOT_FOUND if the agency does not exist.
     */
    @GetMapping("/{id}/agents")
    public Page<UserWithAgencyDTO> getAgentsByAgencyId(@PathVariable Long id, Pageable pageable) {
        RealEstateAgency agency = agencyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agency not found"));
        Page<RealEstateAgent> agents = agentRepository.findByBusinessUser_Agency(agency, pageable);
        return agents.map(UserWithAgencyDTO::new);
    }

    /**
     * Creates a new real estate agency and assigns the requesting user as its manager.
     * Access is restricted to users who are not already affiliated with an agency.
     * @param req The RealEstateAgencyDTO containing the details of the agency to be created.
     * @return A UserWithAgencyDTO representing the newly created manager and their agency.
     * @throws ResponseStatusException with HttpStatus.UNAUTHORIZED if the requesting user is already affiliated with an agency.
     * @throws ResponseStatusException with HttpStatus.INTERNAL_SERVER_ERROR if the requesting user cannot be found in the database.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public UserWithAgencyDTO createAgency(@Valid @RequestBody RealEstateAgencyDTO req) {
        try {
            BaseUser user = userNotAffiliatedWithAgencyService.findByCognitoSub(tokenService.getCognitoSub())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "We could not find you in our database"));

            RealEstateAgency agency = agencyRepository.saveAndFlush(new RealEstateAgency(req.getIban(), req.getName()));

            BusinessUser businessUser = new BusinessUser(user, agency);
            RealEstateManager manager = managerRepository.save(new RealEstateManager(businessUser));
            return new UserWithAgencyDTO(manager);

        } catch (UserIsAlreadyAffiliatedWithAgencyException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are already affiliated with an agency");
        }
    }
}
