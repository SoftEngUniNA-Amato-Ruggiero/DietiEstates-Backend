package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.exceptions.UserIsAlreadyAffiliatedWithAgencyException;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BusinessUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service to handle operations related to users not affiliated with any real estate agency.
 * It checks if a user is already affiliated with an agency before performing certain operations.
 */
@Service
public class UserNotAffiliatedWithAgencyService {
    private final BaseUserRepository userRepository;
    private final BusinessUserRepository businessUserRepository;

    public UserNotAffiliatedWithAgencyService(BaseUserRepository userRepository,
                                              BusinessUserRepository businessUserRepository) {
        this.userRepository = userRepository;
        this.businessUserRepository = businessUserRepository;
    }

    /**
     * Finds a user who is not affiliated with any agency by their Cognito subject.
     *
     * @param cognitoSub The Cognito subject of the user.
     * @return An Optional containing the BaseUser if found and not affiliated, otherwise empty.
     * @throws UserIsAlreadyAffiliatedWithAgencyException if the user is already affiliated with an agency.
     */
    public Optional<BaseUser> findByCognitoSub(String cognitoSub) {
        if (businessUserRepository.findByUser_CognitoSub(cognitoSub).isPresent()) {
            throw new UserIsAlreadyAffiliatedWithAgencyException("User is already affiliated with an agency");
        }

        return userRepository.findByCognitoSub(cognitoSub);
    }

    /**
     * Finds a user who is not affiliated with any agency by their username.
     *
     * @param username The username of the user.
     * @return An Optional containing the BaseUser if found and not affiliated, otherwise empty.
     * @throws UserIsAlreadyAffiliatedWithAgencyException if the user is already affiliated with an agency.
     */
    public Optional<BaseUser> findByUsername(String username) {
        if (businessUserRepository.findByUser_Username(username).isPresent()) {
            throw new UserIsAlreadyAffiliatedWithAgencyException("User is already affiliated with an agency");
        }

        return userRepository.findByUsername(username);
    }
}
