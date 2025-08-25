package it.softengunina.userservice.services;

import it.softengunina.userservice.exceptions.AuthenticationNotFoundException;
import it.softengunina.userservice.exceptions.JwtNotFoundException;
import it.softengunina.userservice.exceptions.MissingClaimException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);

        Jwt result = tokenService.getJwt();
        assertEquals(jwt, result);
    }

    @Test
    void getJwt_throwsAuthenticationNotFoundException() {
        when(securityContext.getAuthentication()).thenReturn(null);
        assertThrows(AuthenticationNotFoundException.class, () -> tokenService.getJwt());
    }

    @Test
    void getJwt_throwsJwtNotFoundException() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("not a jwt");
        assertThrows(JwtNotFoundException.class, () -> tokenService.getJwt());
    }

    @Test
    void getCognitoSub_returnsCognitoSub() {
        when(jwt.getSubject()).thenReturn("cognitoSub");
        assertEquals("cognitoSub", tokenService.getCognitoSub(jwt));
    }

    @Test
    void getCognitoSub_throwsMissingClaimException() {
        when(jwt.getSubject()).thenReturn("");
        assertThrows(MissingClaimException.class, () -> tokenService.getCognitoSub(jwt));
    }

    @Test
    void getEmail_returnsEmail() {
        when(jwt.getClaimAsString("email")).thenReturn("test@email.com");
        assertEquals("test@email.com", tokenService.getEmail(jwt));
    }

    @Test
    void getEmail_throwsMissingClaimException() {
        when(jwt.getClaimAsString("email")).thenReturn("");
        assertThrows(MissingClaimException.class, () -> tokenService.getEmail(jwt));
    }
}