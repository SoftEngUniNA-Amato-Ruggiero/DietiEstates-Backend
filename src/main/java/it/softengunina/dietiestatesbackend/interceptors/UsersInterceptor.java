package it.softengunina.dietiestatesbackend.interceptors;

import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
public class UsersInterceptor implements HandlerInterceptor {
    TokenService tokenService;
    BaseUserRepository userRepository;

    public UsersInterceptor(TokenService tokenService,
                            BaseUserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Jwt jwt = tokenService.getJwt();
        String cognitoSub = tokenService.getCognitoSub(jwt);

        Optional<BaseUser> user = userRepository.findByCognitoSub(cognitoSub);
        if (user.isEmpty()) {
            String email = tokenService.getEmail(jwt);
            userRepository.saveAndFlush(new BaseUser(email, cognitoSub));
        }

        return true;
    }
}
