package it.softengunina.dietiestatesbackend.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.softengunina.dietiestatesbackend.dto.RealEstateAgencyResponseDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.MeResponseDTO;
import it.softengunina.dietiestatesbackend.exceptions.TokenServiceException;
import it.softengunina.dietiestatesbackend.model.NotificationsPreferences;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.NotificationsPreferencesRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BusinessUserRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class MeController {
    private final BaseUserRepository baseUserRepository;
    private final BusinessUserRepository businessUserRepository;
    private final NotificationsPreferencesRepository notificationsPreferencesRepository;
    private final TokenService tokenService;

    MeController(BaseUserRepository baseUserRepository,
                 BusinessUserRepository businessUserRepository,
                 NotificationsPreferencesRepository notificationsPreferencesRepository,
                 TokenService tokenService) {
        this.baseUserRepository = baseUserRepository;
        this.businessUserRepository = businessUserRepository;
        this.notificationsPreferencesRepository = notificationsPreferencesRepository;
        this.tokenService = tokenService;
    }

    /**
     * Retrieves the authenticated user's information along with the agency they may be affiliated with.
     * @return UserWithAgencyDTO containing user and agency details
     * @throws ResponseStatusException with status 404 if notifications preferences are not found
     */
    @GetMapping
    public MeResponseDTO getMe(@RequestAttribute(name = "user") BaseUser user) {
        NotificationsPreferences prefs = notificationsPreferencesRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notifications preferences not found"));

        MeResponseDTO res = new MeResponseDTO(user, prefs);
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
     * @throws ResponseStatusException with status 500 if the user is created but notifications preferences are not found
     */
    @PostMapping
    public MeResponseDTO postMe() {
        try {
            Jwt jwt = tokenService.getJwt();
            String username = tokenService.getEmail(jwt);
            String cognitoSub = tokenService.getCognitoSub(jwt);
            BaseUser user = baseUserRepository.saveAndFlush(new BaseUser(username, cognitoSub));
            NotificationsPreferences prefs = notificationsPreferencesRepository.findByUser_Id(user.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Notifications preferences not created"));
            log.info("User created: {}", user.getUsername());
            return new MeResponseDTO(user, prefs);
        } catch (TokenServiceException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }
    }
}
