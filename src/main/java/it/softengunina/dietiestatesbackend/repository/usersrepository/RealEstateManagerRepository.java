package it.softengunina.dietiestatesbackend.repository.usersrepository;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RealEstateManagerRepository extends JpaRepository<RealEstateManager, Long> {
    Optional<RealEstateManager> findByUser_Username(String username);
    Optional<RealEstateManager> findByUser_CognitoSub(String cognitoSub);
    Page<RealEstateManager> findByAgency(RealEstateAgency agency, Pageable pageable);
}