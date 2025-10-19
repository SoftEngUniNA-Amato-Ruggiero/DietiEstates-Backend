package it.softengunina.dietiestatesbackend.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.softengunina.dietiestatesbackend.dto.RealEstateAgencyResponseDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.BusinessUserResponseDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserResponseDTO;
import it.softengunina.dietiestatesbackend.exceptions.TokenServiceException;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BusinessUserRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for handling requests related to the authenticated user's information.
 * Provides an endpoint to retrieve the user's details along with their associated agency.
 */
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/me")
public class MeController {
    private final BaseUserRepository baseUserRepository;
    private final BusinessUserRepository businessUserRepository;
    private final TokenService tokenService;

    MeController(BaseUserRepository baseUserRepository,
                 BusinessUserRepository businessUserRepository,
                 TokenService tokenService) {
        this.baseUserRepository = baseUserRepository;
        this.businessUserRepository = businessUserRepository;
        this.tokenService = tokenService;
    }

    /**
     * Retrieves the authenticated user's information along with the agency they may be affiliated with.
     * @return UserWithAgencyDTO containing user and agency details
     */
    @GetMapping
    public BusinessUserResponseDTO getMe(@RequestAttribute(name = "user") BaseUser user) {
        BusinessUserResponseDTO res = new BusinessUserResponseDTO();
        res.setUser(new UserResponseDTO(user));
        res.setAgency(null);

        if (!user.getRoles().isEmpty()) {
            businessUserRepository.findById(user.getId())
                    .ifPresent(businessUser -> res.setAgency(new RealEstateAgencyResponseDTO(businessUser.getAgency())));
        }
        return res;
    }

    /**
     * Creates a new user in the system based on the information extracted from the JWT token.
     * If the user already exists, a 409 Conflict status is returned.
     * If the token is invalid, a 401 Unauthorized status is returned.
     * @return UserResponseDTO containing the created user's details
     * @throws ResponseStatusException with status 401 if the token is invalid
     * @throws ResponseStatusException with status 409 if the user already exists
     */
    @PostMapping
    public UserResponseDTO postMe() {
        try {
            Jwt jwt = tokenService.getJwt();
            String username = tokenService.getEmail(jwt);
            String cognitoSub = tokenService.getCognitoSub(jwt);
            BaseUser user = baseUserRepository.save(new BaseUser(username, cognitoSub));
            return new UserResponseDTO(user);
        } catch (TokenServiceException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }
    }
}
