package it.softengunina.userservice.filter;

import it.softengunina.userservice.exceptions.AuthenticationNotFoundException;
import it.softengunina.userservice.model.User;
import it.softengunina.userservice.repository.UserRepository;
import it.softengunina.userservice.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    UserRepository<User> userRepository;
    TokenService tokenService;

    public JwtRequestFilter(UserRepository<User> userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            log.info("Processing request: {} {}", request.getMethod(), request.getRequestURI());

            String authHeader = request.getHeader("Authorization");
            if (authHeader != null) {
                log.info("Authorization = {}", authHeader);
            } else {
                log.warn("Authorization header is missing.");
            }

            Jwt jwt = tokenService.getJwt();
            Map<String, Object> claims = tokenService.getClaims(jwt);
            String cognitoSub = tokenService.getCognitoSub(jwt);
            String username = tokenService.getEmail(jwt);

            log.info("Bearer: {}", jwt.getTokenValue());
            log.info("claims: {}", claims);
            log.info("Cognito Sub: {}", cognitoSub);
            log.info("Username: {}", username);

            if (userRepository.findByCognitoSub(cognitoSub).isEmpty()) {
                User user = userRepository.save(new User(username, cognitoSub));
                log.info("New user saved: {}", user.getUsername());
            }
        } catch (AuthenticationNotFoundException e) {
            log.info("Request from unauthenticated user.");
        } catch (Exception e) {
            log.error("An unexpected error occurred while processing the JWT.", e);
        }
        filterChain.doFilter(request, response);
    }
}
