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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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
            User user = tokenService.getUser();
            if (userRepository.findByCognitoSub(user.getCognitoSub()).isEmpty()) {
                userRepository.save(user);
            }
            log.info("Request from {}", user.getEmail());
        } catch (AuthenticationNotFoundException e) {
            log.info("Request from unauthenticated user.");
        } catch (Exception e) {
            log.error("An unexpected error occurred while processing the JWT.", e);
        }
        filterChain.doFilter(request, response);
    }
}
