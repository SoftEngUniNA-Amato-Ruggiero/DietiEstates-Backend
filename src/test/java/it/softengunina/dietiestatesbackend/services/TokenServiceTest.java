package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.exceptions.AuthenticationNotFoundException;
import it.softengunina.dietiestatesbackend.exceptions.JwtNotFoundException;
import it.softengunina.dietiestatesbackend.exceptions.MissingClaimException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {
    @Mock
    SecurityContext securityContext;
    @Mock
    Authentication authentication;
    @Mock
    Jwt jwt;

    AutoCloseable mocks;

    TokenService tokenService;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        tokenService = new TokenService();
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void getJwt_returnsJwt() {
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(jwt);

        Jwt result = tokenService.getJwt();
        assertEquals(jwt, result);
    }

    @Test
    void getJwt_throwsAuthenticationNotFoundException() {
        Mockito.when(securityContext.getAuthentication()).thenReturn(null);
        assertThrows(AuthenticationNotFoundException.class, () -> tokenService.getJwt());
    }

    @Test
    void getJwt_throwsJwtNotFoundException() {
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn("not a jwt");
        assertThrows(JwtNotFoundException.class, () -> tokenService.getJwt());
    }

    @Test
    void getCognitoSub_returnsCognitoSub() {
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(jwt);
        Mockito.when(jwt.getSubject()).thenReturn("cognitoSub");
        assertEquals("cognitoSub", tokenService.getCognitoSub());
    }

    @Test
    void getCognitoSub_throwsMissingClaimException() {
        Mockito.when(jwt.getSubject()).thenReturn("");
        assertThrows(MissingClaimException.class, () -> tokenService.getCognitoSub(jwt));
    }

    @Test
    void getEmail_returnsEmail() {
        Mockito.when(jwt.getClaimAsString("email")).thenReturn("test@email.com");
        assertEquals("test@email.com", tokenService.getEmail(jwt));
    }

    @Test
    void getEmail_throwsMissingClaimException() {
        Mockito.when(jwt.getClaimAsString("email")).thenReturn("");
        assertThrows(MissingClaimException.class, () -> tokenService.getEmail(jwt));
    }
}