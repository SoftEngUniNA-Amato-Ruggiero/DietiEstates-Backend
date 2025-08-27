package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Optional;

class UserSyncServiceTest {

    UserSyncService userSyncService;
    AutoCloseable mocks;

    @Mock
    UserRepository<BaseUser> userRepository;
    @Mock
    TokenService tokenService;

    @Mock
    AuthenticationSuccessEvent event;
    @Mock
    JwtAuthenticationToken authenticationToken;
    @Mock
    Jwt jwt;

    String username = "testName";
    String sub = "testSub";

    @BeforeEach
    void setUp() {
        mocks = org.mockito.MockitoAnnotations.openMocks(this);
        Mockito.when(event.getAuthentication()).thenReturn(authenticationToken);
        Mockito.when(authenticationToken.getToken()).thenReturn(jwt);

        Mockito.when(userRepository.findByCognitoSub(sub)).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(Mockito.any(BaseUser.class))).thenAnswer(i -> i.getArguments()[0]);

        userSyncService = new UserSyncService(userRepository, tokenService);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void handleAuthentication() {
        Mockito.when(tokenService.getCognitoSub(jwt)).thenReturn(sub);
        Mockito.when(tokenService.getEmail(jwt)).thenReturn(username);

        userSyncService.handleAuthentication(event);

        Mockito.verify(userRepository).save(Mockito.argThat(u ->
                u.getUsername().equals(username) && u.getCognitoSub().equals(sub)
        ));
    }
}