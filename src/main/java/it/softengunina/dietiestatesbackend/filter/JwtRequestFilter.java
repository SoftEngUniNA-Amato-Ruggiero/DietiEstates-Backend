package it.softengunina.dietiestatesbackend.filter;

import it.softengunina.dietiestatesbackend.exceptions.AuthenticationNotFoundException;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.UserRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
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

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    UserRepository<BaseUser> userRepository;
    TokenService tokenService;

    public JwtRequestFilter(UserRepository<BaseUser> userRepository, TokenService tokenService) {
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
            String cognitoSub = tokenService.getCognitoSub(jwt);
            String username = tokenService.getEmail(jwt);

            if (userRepository.findByCognitoSub(cognitoSub).isEmpty()) {
                BaseUser user = userRepository.save(new BaseUser(username, cognitoSub));
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
