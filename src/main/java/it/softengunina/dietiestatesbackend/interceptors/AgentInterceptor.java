package it.softengunina.dietiestatesbackend.interceptors;

import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateAgentRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AgentInterceptor implements HandlerInterceptor {
    RealEstateAgentRepository agentRepository;
    TokenService tokenService;

    public AgentInterceptor(RealEstateAgentRepository agentRepository,
            TokenService tokenService) {
        this.agentRepository = agentRepository;
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull Object handler) throws Exception {
        if (request.getMethod().equals("GET")) {
            return true;
        }

        try {
            RealEstateAgent agent = agentRepository.findByBusinessUser_User_CognitoSub(tokenService.getCognitoSub())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not an agent"));
            request.setAttribute("agent", agent);
            return true;
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
            return false;
        }
    }
}
