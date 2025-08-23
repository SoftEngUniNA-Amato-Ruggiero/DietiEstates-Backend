package it.softengunina.userservice.repository;

import it.softengunina.userservice.model.RealEstateAgent;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface RealEstateAgentRepository<T extends RealEstateAgent>  extends UserRepository<T> {
    @Query("SELECT a FROM RealEstateAgent a WHERE a.agency.id = :agencyId")
    Set<T> findByAgencyId(Long agencyId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value="INSERT INTO real_estate_agents (id, agency_id) VALUES (:id, :agencyId)", nativeQuery=true)
    void insertAgent(@Param("id") Long id, @Param("agencyId") Long agencyId);
}
