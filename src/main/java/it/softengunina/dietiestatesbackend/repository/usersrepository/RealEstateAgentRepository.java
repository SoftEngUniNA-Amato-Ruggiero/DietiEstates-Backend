package it.softengunina.dietiestatesbackend.repository.usersrepository;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RealEstateAgentRepository extends JpaRepository<RealEstateAgent, Long> {
    Optional<RealEstateAgent> findByBusinessUser_User_Username(String username);
    Optional<RealEstateAgent> findByBusinessUser_User_CognitoSub(String cognitoSub);

    Page<RealEstateAgent> findByBusinessUser_Agency(RealEstateAgency agency, Pageable pageable);

    Optional<RealEstateAgent> findByBusinessUser_AgencyAndBusinessUser_User_Username(RealEstateAgency agency, String username);
    Optional<RealEstateAgent> findByBusinessUser_AgencyAndBusinessUser_User_CognitoSub(RealEstateAgency agency, String cognitoSub);

    boolean existsByBusinessUser_User_Username(String username);
    boolean existsByBusinessUser_User_CognitoSub(String cognitoSub);
}
