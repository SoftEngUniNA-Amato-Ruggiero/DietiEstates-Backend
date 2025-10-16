package it.softengunina.dietiestatesbackend.interceptors;

import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class UsersInterceptor implements HandlerInterceptor {
    TokenService tokenService;
    BaseUserRepository userRepository;

    public UsersInterceptor(TokenService tokenService,
                            BaseUserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        try {
            Jwt jwt = tokenService.getJwt();
            String cognitoSub = tokenService.getCognitoSub(jwt);

            if (!(request.getRequestURI().contains("/me") && request.getMethod().equals("POST"))) {
                BaseUser user = userRepository.findByCognitoSub(cognitoSub)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Authenticated user not found in the database"));
                request.setAttribute("user", user);
            }
            return true;

        } catch (Exception e) {
            long tid = Thread.currentThread().threadId();
            log.warn("preHandle from request of {} on threadId {} caught exception {}:\n{}", request.getRequestURI(), tid, e.getClass().getName(), e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            return false;
        }
    }
}
