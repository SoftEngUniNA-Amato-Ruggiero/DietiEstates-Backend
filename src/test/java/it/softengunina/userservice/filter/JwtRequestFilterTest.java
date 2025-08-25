package it.softengunina.userservice.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import it.softengunina.userservice.exceptions.AuthenticationNotFoundException;
import it.softengunina.userservice.exceptions.MissingClaimException;
import it.softengunina.userservice.exceptions.JwtNotFoundException;
import it.softengunina.userservice.model.User;
import it.softengunina.userservice.repository.UserRepository;
import it.softengunina.userservice.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class JwtRequestFilterTest {
    @Mock
    UserRepository<User> userRepository;
    @Mock
    TokenService tokenService;

    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    FilterChain chain;
    @Mock
    Jwt jwt;

    AutoCloseable mocks;

    Logger logger;
    ListAppender<ILoggingEvent> appender;

    JwtRequestFilter filter;

    String testSub = "testSub";
    String testEmail = "test@email.com";
    Map<String, Object> claims = Map.of("cognitoSub", testSub, "email", testEmail);

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        when(tokenService.getCognitoSub(jwt)).thenReturn(testSub);
        when(tokenService.getEmail(jwt)).thenReturn(testEmail);

        logger = (Logger) LoggerFactory.getLogger(JwtRequestFilter.class);
        appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);

        filter = new JwtRequestFilter(userRepository, tokenService);
    }

    @AfterEach
    void tearDown() throws Exception {
        logger.detachAppender(appender);
        mocks.close();
    }

    @Test
    void doFilterInternal_saveNewUser() throws ServletException, IOException {
        when(tokenService.getJwt()).thenReturn(jwt);
        when(userRepository.findByCognitoSub(testSub)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        filter.doFilterInternal(request, response, chain);

        assertAll(
                () -> assertThat(appender.list)
                        .noneMatch(event -> event.getLevel() == Level.WARN || event.getLevel() == Level.ERROR),
                () -> assertThat(appender.list)
                        .anyMatch(event -> event.getFormattedMessage().contains("New user saved")),
                () -> assertThat(appender.list)
                        .noneMatch(event -> event.getFormattedMessage().contains("Request from unauthenticated user"))
        );
    }

    @Test
    void doFilterInternal_userFound() throws ServletException, IOException {
        when(tokenService.getJwt()).thenReturn(jwt);
        when(userRepository.findByCognitoSub(testSub)).thenReturn(Optional.of(new User(testEmail, testSub)));

        filter.doFilterInternal(request, response, chain);

        assertAll(
                () -> assertThat(appender.list)
                        .noneMatch(event -> event.getLevel() == Level.WARN || event.getLevel() == Level.ERROR),
                () -> assertThat(appender.list)
                        .noneMatch(event -> event.getFormattedMessage().contains("Request from unauthenticated user"))
        );
    }

    @Test
    void doFilterInternal_unauthenticated() throws ServletException, IOException {
        when(tokenService.getJwt()).thenThrow(new AuthenticationNotFoundException("No Authentication found in SecurityContext."));

        filter.doFilterInternal(request, response, chain);
        assertAll(
                () -> assertThat(appender.list)
                        .noneMatch(event -> event.getLevel() == Level.WARN || event.getLevel() == Level.ERROR),
                () -> assertThat(appender.list)
                        .anyMatch(event -> event.getFormattedMessage().contains("Request from unauthenticated user"))
        );
    }

    @Test
    void doFilterInternal_jwtNotFound() throws ServletException, IOException {
        when(tokenService.getJwt()).thenThrow(new JwtNotFoundException("Authentication object is not an instance of Jwt."));

        filter.doFilterInternal(request, response, chain);
        assertAll(
                () -> assertThat(appender.list)
                        .anyMatch(event -> event.getLevel() == Level.ERROR && event.getFormattedMessage().contains("An unexpected error occurred while processing the JWT.")),
                () -> assertThat(appender.list)
                        .noneMatch(event -> event.getFormattedMessage().contains("New user saved"))
        );
    }

    @Test
    void doFilterInternal_badJwt() throws ServletException, IOException {
        when(tokenService.getJwt()).thenReturn(jwt);
        when(userRepository.findByCognitoSub(testSub)).thenThrow(new MissingClaimException("Cognito sub claim is missing or empty"));

        filter.doFilterInternal(request, response, chain);

        assertAll(
                () -> assertThat(appender.list)
                        .anyMatch(event -> event.getLevel() == Level.ERROR && event.getFormattedMessage().contains("An unexpected error occurred while processing the JWT.")),
                () -> assertThat(appender.list)
                        .noneMatch(event -> event.getFormattedMessage().contains("New user saved"))
        );
    }
}