package it.softengunina.dietiestatesbackend.repository.usersrepository;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface RealEstateAgentRepository<T extends RealEstateAgent>  extends BaseUserRepository<T> {
    @Query("SELECT a FROM RealEstateAgent a WHERE a.agency = :agency")
    Page<T> findByAgency(RealEstateAgency agency, Pageable pageable);

    /** Promotes the BaseUser with the given id to a RealEstateAgent by inserting a row in the real_estate_agents table.
     * The usage of this method is discouraged, prefer using UserPromotionStrategy instead.
     * @param id the id of the user to promote
     * @param agencyId the id of the agency the agent will belong to
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value="INSERT INTO real_estate_agents (id, agency_id) VALUES (:id, :agencyId)", nativeQuery=true)
    void insertAgent(@Param("id") Long id, @Param("agencyId") Long agencyId);

    /** Demotes the RealEstateAgent with the given id to a BaseUser without deleting the BaseUser from the database.
     * The usage of this method is discouraged, prefer using UserDemotionStrategy instead.
     * NEVER use this with the id of a RealEstateManager, or it will result in a DataIntegrityViolationException.
     * To demote a RealEstateManager to a BaseUser, call RealEstateManagerRepository.demoteManager before this method.
     * @param id the id of the agent to demote
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value="DELETE FROM real_estate_agents WHERE id = :id", nativeQuery=true)
    void demoteAgent(@Param("id") Long id);
}
