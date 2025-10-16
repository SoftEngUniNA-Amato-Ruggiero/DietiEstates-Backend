package it.softengunina.dietiestatesbackend.interceptors;

import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateManagerRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ManagerInterceptor implements HandlerInterceptor {
    RealEstateManagerRepository managerRepository;
    TokenService tokenService;

    public ManagerInterceptor(RealEstateManagerRepository managerRepository,
                              TokenService tokenService) {
        this.managerRepository = managerRepository;
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        try {
            RealEstateManager manager = managerRepository.findByBusinessUser_User_CognitoSub(tokenService.getCognitoSub())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a manager"));
            request.setAttribute("manager", manager);
            return true;
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
            return false;
        }
    }
}
