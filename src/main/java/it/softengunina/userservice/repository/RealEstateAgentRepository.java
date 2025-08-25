package it.softengunina.userservice.repository;

import it.softengunina.userservice.model.RealEstateAgency;
import it.softengunina.userservice.model.RealEstateAgent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface RealEstateAgentRepository<T extends RealEstateAgent>  extends UserRepository<T> {
    @Query("SELECT a FROM RealEstateAgent a WHERE a.agency = :agency")
    Page<T> findByAgency(RealEstateAgency agency, Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value="INSERT INTO real_estate_agents (id, agency_id) VALUES (:id, :agencyId)", nativeQuery=true)
    void insertAgent(@Param("id") Long id, @Param("agencyId") Long agencyId);
}
