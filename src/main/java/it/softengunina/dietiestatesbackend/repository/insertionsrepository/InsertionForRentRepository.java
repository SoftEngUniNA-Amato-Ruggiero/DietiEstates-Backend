package it.softengunina.dietiestatesbackend.repository.insertionsrepository;

import it.softengunina.dietiestatesbackend.dto.searchdto.SearchRequestForRentDTO;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionForRent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface InsertionForRentRepository extends BaseInsertionRepository<InsertionForRent> {
    Page<InsertionForRent> findByRent(Double rent, Pageable pageable);
    Page<InsertionForRent> findByRentLessThanEqual(Double rent, Pageable pageable);
    Page<InsertionForRent> findByRentGreaterThanEqual(Double rent, Pageable pageable);

    @Transactional
    @Query("SELECT i FROM InsertionForRent i LEFT JOIN i.tags t ON (:tagCount > 0 AND t.name in :tags) " +
            "WHERE function('ST_DWithin', i.address.location, function('ST_SetSRID', function('ST_MakePoint', :#{#req.lng}, :#{#req.lat}), 4326), :#{#req.distance}) = true " +
            "AND (:#{#req.maxRent} IS NULL OR i.rent <= :#{#req.maxRent}) " +
            "AND (:#{#req.minSize} IS NULL OR i.details.size >= :#{#req.minSize}) " +
            "AND (:#{#req.minNumberOfRooms} IS NULL OR i.details.numberOfRooms >= :#{#req.minNumberOfRooms}) " +
            "AND (:#{#req.maxFloor} IS NULL OR i.details.floor <= :#{#req.maxFloor}) " +
            "AND (:#{#req.hasElevator} IS NULL OR i.details.hasElevator = :#{#req.hasElevator}) " +
            "GROUP BY i " +
            "HAVING :tagCount = 0 OR COUNT(t) = :tagCount")
    Page<InsertionForRent> search(@Param("req") SearchRequestForRentDTO req, Set<String> tags, Integer tagCount, Pageable pageable);
}
