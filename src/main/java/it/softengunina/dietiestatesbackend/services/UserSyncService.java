package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.User;
import it.softengunina.dietiestatesbackend.repository.usersrepository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserSyncService {
    UserRepository<BaseUser> userRepository;
    TokenService tokenService;

    public UserSyncService(UserRepository<BaseUser> userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @EventListener
    @Async
    public void handleAuthentication(AuthenticationSuccessEvent event) {
        if (event.getAuthentication() instanceof JwtAuthenticationToken authenticationToken) {
            syncUser(authenticationToken.getToken());
        }
    }

    private void syncUser(Jwt jwt) {
        String cognitoSub = tokenService.getCognitoSub(jwt);
        String email = tokenService.getEmail(jwt);

        if (userRepository.findByCognitoSub(cognitoSub).isEmpty()) {
            User user = userRepository.save(new BaseUser(email, cognitoSub));
            log.info("New user saved: {}", user.getUsername());
        }
    }
}
