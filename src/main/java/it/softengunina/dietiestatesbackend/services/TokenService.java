package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.exceptions.AuthenticationNotFoundException;
import it.softengunina.dietiestatesbackend.exceptions.MissingClaimException;
import it.softengunina.dietiestatesbackend.exceptions.JwtNotFoundException;
import lombok.NonNull;
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
    /**
     * Retrieves the JWT token from the current security context.
     *
     * @return the JWT token
     * @throws AuthenticationNotFoundException if no authentication is found in the security context
     * @throws JwtNotFoundException            if the authentication object is not a JWT
     */
    public Jwt getJwt() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = getAuthenticationFromSecurityContext(securityContext);
        return getJwtFromAuthentication(authentication);
    }

    /**
     * Extracts the Cognito subject (sub) claim from the given JWT token.
     *
     * @param jwt the JWT token
     * @return the Cognito subject
     * @throws MissingClaimException if the sub claim is missing or empty
     */
    public String getCognitoSub(@NonNull Jwt jwt) {
        String cognitoSub = jwt.getSubject();
        if (cognitoSub == null || cognitoSub.isEmpty()) {
            throw new MissingClaimException("Cognito sub claim is missing or empty.");
        }
        return cognitoSub;
    }

    /**
     * Extracts the Cognito subject (sub) claim from the JWT token in the current security context.
     *
     * @return the Cognito subject
     * @throws AuthenticationNotFoundException if no authentication is found in the security context
     * @throws JwtNotFoundException            if the authentication object is not a JWT
     * @throws MissingClaimException           if the sub claim is missing or empty
     */
    public String getCognitoSub() {
        Jwt jwt = getJwt();
        return getCognitoSub(jwt);
    }

    /**
     * Extracts the email claim from the given JWT token.
     *
     * @param jwt the JWT token
     * @return the email
     * @throws MissingClaimException if the email claim is missing or empty
     */
    public String getEmail(@NonNull Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        if (email == null || email.isEmpty()) {
            throw new MissingClaimException("Email claim is missing or empty.");
        }
        return email;
    }

    private Authentication getAuthenticationFromSecurityContext(@NonNull SecurityContext securityContext) {
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null) {
            throw new AuthenticationNotFoundException("No Authentication found in SecurityContext.");
        }
        return authentication;
    }

    private Jwt getJwtFromAuthentication(@NonNull Authentication authentication) {
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt;
        } else {
            throw new JwtNotFoundException("Authentication object is not an instance of Jwt.");
        }
    }
}
