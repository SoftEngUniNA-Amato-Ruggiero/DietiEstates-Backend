package it.softengunina.userservice.repository;

import it.softengunina.userservice.model.RealEstateManager;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RealEstateManagerRepository extends RealEstateAgentRepository<RealEstateManager> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value="INSERT INTO real_estate_managers (id) VALUES (:id)", nativeQuery=true)
    void insertManager(@Param("id") Long id);
}