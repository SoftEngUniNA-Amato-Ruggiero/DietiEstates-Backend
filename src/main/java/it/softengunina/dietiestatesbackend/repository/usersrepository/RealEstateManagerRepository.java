package it.softengunina.dietiestatesbackend.repository.usersrepository;

import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RealEstateManagerRepository extends RealEstateAgentRepository<RealEstateManager> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value="INSERT INTO real_estate_managers (id) VALUES (:id)", nativeQuery=true)
    void insertManager(@Param("id") Long id);

    /** Demotes the RealEstateManager with the given id to a RealEstateAgent without deleting the RealEstateAgent from the database.
     * @param id the id of the manager to demote
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value="DELETE FROM real_estate_managers WHERE id = :id", nativeQuery=true)
    void demoteManager(@Param("id") Long id);
}