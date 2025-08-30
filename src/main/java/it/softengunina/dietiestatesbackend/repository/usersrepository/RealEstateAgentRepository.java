package it.softengunina.dietiestatesbackend.repository.usersrepository;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RealEstateAgentRepository extends JpaRepository<RealEstateAgent, Long> {
    Optional<RealEstateAgent> findByUser_Username(String username);
    Optional<RealEstateAgent> findByUser_CognitoSub(String cognitoSub);
    Page<RealEstateAgent> findByAgency(RealEstateAgency agency, Pageable pageable);
}
