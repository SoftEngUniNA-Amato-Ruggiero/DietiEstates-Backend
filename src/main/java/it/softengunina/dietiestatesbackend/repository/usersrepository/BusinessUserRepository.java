package it.softengunina.dietiestatesbackend.repository.usersrepository;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessUserRepository extends JpaRepository<BusinessUser, Long> {

    Optional<BusinessUser> findByUser_Username(String username);

    Optional<BusinessUser> findByUser_CognitoSub(String cognitoSub);

    Page<BusinessUser> findByAgency(RealEstateAgency agency, Pageable pageable);
    Optional<BusinessUser> findByAgencyAndUser_Username(RealEstateAgency agency, String username);
    Optional<BusinessUser> findByAgencyAndUser_CognitoSub(RealEstateAgency agency, String cognitoSub);

    boolean existsByUser_Username(String username);
    boolean existsByUser_CognitoSub(String cognitoSub);
}
