package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.exceptions.AuthenticationNotFoundException;
import it.softengunina.dietiestatesbackend.exceptions.MissingClaimException;
import it.softengunina.dietiestatesbackend.exceptions.JwtNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

/**
 * Service for extracting and validating JWT tokens from the security context.
 */
@Service
@Slf4j
public class TokenService {
    public Jwt getJwt() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = getAuthenticationFromSecurityContext(securityContext);
        return getJwtFromAuthentication(authentication);
    }

    public String getCognitoSub(Jwt jwt) {
        String cognitoSub = jwt.getSubject();
        if (cognitoSub == null || cognitoSub.isEmpty()) {
            throw new MissingClaimException("Cognito sub claim is missing or empty.");
        }
        return cognitoSub;
    }

    public String getCognitoSub() {
        Jwt jwt = getJwt();
        return getCognitoSub(jwt);
    }

    public String getEmail(Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        if (email == null || email.isEmpty()) {
            throw new MissingClaimException("Email claim is missing or empty.");
        }
        return email;
    }

    private Authentication getAuthenticationFromSecurityContext(SecurityContext securityContext) {
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null) {
            throw new AuthenticationNotFoundException("No Authentication found in SecurityContext.");
        }
        return authentication;
    }

    private Jwt getJwtFromAuthentication(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt;
        } else {
            throw new JwtNotFoundException("Authentication object is not an instance of Jwt.");
        }
    }
}
