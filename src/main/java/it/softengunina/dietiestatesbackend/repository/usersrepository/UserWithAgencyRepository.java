package it.softengunina.dietiestatesbackend.repository.usersrepository;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.UserWithAgency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserWithAgencyRepository<T extends UserWithAgency> extends JpaRepository<T, Long> {
    Optional<T> findByUser_Username(String username);
    Optional<T> findByUser_CognitoSub(String cognitoSub);

    Page<T> findByAgency(RealEstateAgency agency, Pageable pageable);
    Optional<T> findByAgencyAndUser_Username(RealEstateAgency agency, String username);
    Optional<T> findByAgencyAndUser_CognitoSub(RealEstateAgency agency, String cognitoSub);

    Optional<RealEstateAgency> findAgencyByUser_Username(String username);
    Optional<RealEstateAgency> findAgencyByUser_CognitoSub(String cognitoSub);
}
