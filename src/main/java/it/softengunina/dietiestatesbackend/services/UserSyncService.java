package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.User;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service to synchronize users to the local database upon successful authentication.
 * Listens for authentication success events and ensures that users are created in the local database if they do not already exist.
 */
@Service
@Slf4j
public class UserSyncService {
    BaseUserRepository userRepository;
    TokenService tokenService;

    public UserSyncService(BaseUserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    /**
     * Handles successful authentication events.
     * If the authentication is via a valid JWT, it synchronizes the user to the local database.
     *
     * @param event The authentication success event.
     */
    @EventListener
    @Transactional
    public void handleAuthentication(AuthenticationSuccessEvent event) {
        if (event.getAuthentication() instanceof JwtAuthenticationToken authenticationToken) {
            try {
                User user = syncUser(authenticationToken.getToken());
                log.debug("User synchronized: {}", user);
            } catch (Exception e) {
                log.error("Error synchronizing user on authentication: {}", e.getMessage(), e);
            }
        }
    }

    private User syncUser(Jwt jwt) {
        String cognitoSub = tokenService.getCognitoSub(jwt);
        String email = tokenService.getEmail(jwt);

        return userRepository.findByCognitoSub(cognitoSub)
                .orElseGet(() -> saveAndSync(new BaseUser(email, cognitoSub)));
    }

    private BaseUser saveAndSync(BaseUser user) {
        try {
            return userRepository.saveAndFlush(user);

        } catch (DataIntegrityViolationException e) {
            return userRepository.findByCognitoSub(user.getCognitoSub())
                    .orElseThrow(() -> new IllegalStateException("User should exist after integrity violation", e));
        }
    }
}
