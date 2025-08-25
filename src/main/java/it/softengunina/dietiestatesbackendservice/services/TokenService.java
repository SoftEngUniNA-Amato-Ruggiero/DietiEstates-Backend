package it.softengunina.dietiestatesbackendservice.services;

import it.softengunina.dietiestatesbackendservice.exceptions.AuthenticationNotFoundException;
import it.softengunina.dietiestatesbackendservice.exceptions.MissingClaimException;
import it.softengunina.dietiestatesbackendservice.exceptions.JwtNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TokenService {
    public Jwt getJwt() {
        Authentication authentication = getAuthentication();
        return getJwt(authentication);
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

    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AuthenticationNotFoundException("No Authentication found in SecurityContext.");
        }
        return authentication;
    }

    private Jwt getJwt(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt;
        } else {
            throw new JwtNotFoundException("Authentication object is not an instance of Jwt.");
        }
    }
}
