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

            Jwt jwt = tokenService.getJwt();
            Map<String, Object> claims = tokenService.getClaims(jwt);
            log.info("claims: {}", claims);
            User user = tokenService.getUser(jwt);
            log.info("user: {}", user);

            if (userRepository.findByCognitoSub(user.getCognitoSub()).isEmpty()) {
                userRepository.save(user);
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
