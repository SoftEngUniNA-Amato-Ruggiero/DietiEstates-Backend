package it.softengunina.dietiestatesbackend.repository.usersrepository;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RealEstateManagerRepository extends JpaRepository<RealEstateManager, Long> {
    Optional<RealEstateManager> findByBusinessUser_User_Username(String username);
    Optional<RealEstateManager> findByBusinessUser_User_CognitoSub(String cognitoSub);

    Page<RealEstateManager> findByBusinessUser_Agency(RealEstateAgency agency, Pageable pageable);
    Optional<RealEstateManager> findByBusinessUser_AgencyAndBusinessUser_User_Username(RealEstateAgency agency, String username);
    Optional<RealEstateManager> findByBusinessUser_AgencyAndBusinessUser_User_CognitoSub(RealEstateAgency agency, String cognitoSub);

    boolean existsByBusinessUser_User_Username(String username);
    boolean existsByBusinessUser_User_CognitoSub(String cognitoSub);
}