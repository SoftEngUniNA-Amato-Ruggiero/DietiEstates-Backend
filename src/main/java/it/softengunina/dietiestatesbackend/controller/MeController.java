package it.softengunina.dietiestatesbackend.controller;

import it.softengunina.dietiestatesbackend.dto.usersdto.UserWithAgencyDTO;
import it.softengunina.dietiestatesbackend.model.users.UserWithAgency;
import it.softengunina.dietiestatesbackend.repository.usersrepository.UserWithAgencyRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for handling requests related to the authenticated user's information.
 * Provides an endpoint to retrieve the user's details along with their associated agency.
 */
@RestController
@RequestMapping("/me")
public class MeController {
    private final UserWithAgencyRepository<UserWithAgency> repository;
    private final TokenService tokenService;

    MeController(UserWithAgencyRepository<UserWithAgency> repository,
                 TokenService tokenService) {
        this.repository = repository;
        this.tokenService = tokenService;
    }

    /**
     * Retrieves the authenticated user's information along with the agency they are affiliated with.
     *
     * @return UserWithAgencyDTO containing user and agency details
     * @throws ResponseStatusException if the user is not affiliated with any agency
     */
    @GetMapping("/agency")
    public UserWithAgencyDTO getAgency() {
        String cognitoSub = tokenService.getCognitoSub();

        UserWithAgency user = repository.findFirstByUser_CognitoSub(cognitoSub)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not affiliated with any agency"));

        return new UserWithAgencyDTO(user);
    }
}
