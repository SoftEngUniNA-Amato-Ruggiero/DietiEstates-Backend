package it.softengunina.dietiestatesbackend.controller;

import it.softengunina.dietiestatesbackend.dto.usersdto.BusinessUserResponseDTO;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BusinessUserRepository;
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
    private final BusinessUserRepository businessUserRepository;
    private final TokenService tokenService;

    MeController(BusinessUserRepository businessUserRepository,
                 TokenService tokenService) {
        this.businessUserRepository = businessUserRepository;
        this.tokenService = tokenService;
    }

    /**
     * Retrieves the authenticated user's information along with the agency they are affiliated with.
     * If the user is not affiliated with any agency, it throws a 404 NOT FOUND error.
     * This is because the base user information are already in the jwt.
     * @return UserWithAgencyDTO containing user and agency details
     * @throws ResponseStatusException if the user is not affiliated with any agency
     */
    @GetMapping
    public BusinessUserResponseDTO getMe() {
        String cognitoSub = tokenService.getCognitoSub();

        BusinessUser user = businessUserRepository.findByUser_CognitoSub(cognitoSub)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not affiliated with any agency"));
        return new BusinessUserResponseDTO(user);
    }
}
