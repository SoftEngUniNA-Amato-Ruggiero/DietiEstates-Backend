package it.softengunina.userservice.services;

import it.softengunina.userservice.exceptions.AuthenticationNotFoundException;
import it.softengunina.userservice.exceptions.JwtNotFoundException;
import it.softengunina.userservice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class TokenService {

    public Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AuthenticationNotFoundException("No Authentication found in SecurityContext.");
        }
        return authentication;
    }

    public Jwt getJwt(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt;
        } else {
            throw new JwtNotFoundException("Authentication object is not an instance of Jwt.");
        }
    }

    public Jwt getJwt() {
        Authentication authentication = getAuthentication();
        return getJwt(authentication);
    }

    public String getCognitoSub(Jwt jwt) {
        String cognitoSub = jwt.getSubject();
        if (cognitoSub == null || cognitoSub.isEmpty()) {
            throw new JwtNotFoundException("Cognito sub claim is missing or empty.");
        }
        return cognitoSub;
    }

    public String getCognitoSub() {
        Jwt jwt = getJwt();
        return getCognitoSub(jwt);
    }

    public String getUsername(Jwt jwt) {
        return jwt.getClaimAsString("username");
    }

    public String getUsername() {
        Jwt jwt = getJwt();
        return getUsername(jwt);
    }

    public Map<String, Object> getClaims(Jwt jwt) {
        return jwt.getClaims();
    }

    public Map<String, Object> getClaims() {
        Jwt jwt = getJwt();
        return jwt.getClaims();
    }

    public User getUser() {
        Jwt jwt = getJwt();
        return new User(getUsername(jwt), getCognitoSub(jwt));
    }
}
