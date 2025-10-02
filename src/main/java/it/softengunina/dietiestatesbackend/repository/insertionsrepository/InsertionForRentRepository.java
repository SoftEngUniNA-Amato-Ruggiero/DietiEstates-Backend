package it.softengunina.dietiestatesbackend.repository.insertionsrepository;

import it.softengunina.dietiestatesbackend.model.insertions.InsertionForRent;
import org.locationtech.jts.geom.Point;
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
    @Query("SELECT i FROM #{#entityName} i " +
            "WHERE function('ST_DWithin', i.address.location, :point, :distance) = true " +
            "AND (:minSize IS NULL OR i.details.size >= :minSize) " +
            "AND (:minNumberOfRooms IS NULL OR i.details.numberOfRooms >= :minNumberOfRooms) " +
            "AND (:maxFloor IS NULL OR i.details.floor <= :maxFloor) " +
            "AND (:hasElevator IS NULL OR i.details.hasElevator = :hasElevator) " +
            "AND (:maxRent IS NULL OR i.rent <= :maxRent)")
    Page<InsertionForRent> searchWithoutTags(@Param("point") Point point,
                                             @Param("distance") Double distance,
                                             @Param("minSize") Double minSize,
                                             @Param("minNumberOfRooms") Integer minNumberOfRooms,
                                             @Param("maxFloor") Integer maxFloor,
                                             @Param("hasElevator") Boolean hasElevator,
                                             @Param("maxRent") Double maxRent,
                                             Pageable pageable);

    @Transactional
    @Query("SELECT i FROM #{#entityName} i JOIN i.tags t " +
            "WHERE function('ST_DWithin', i.address.location, :point, :distance) = true " +
            "AND (:minSize IS NULL OR i.details.size >= :minSize) " +
            "AND (:minNumberOfRooms IS NULL OR i.details.numberOfRooms >= :minNumberOfRooms) " +
            "AND (:maxFloor IS NULL OR i.details.floor <= :maxFloor) " +
            "AND (:hasElevator IS NULL OR i.details.hasElevator = :hasElevator) " +
            "AND (:maxRent IS NULL OR i.rent <= :maxRent) " +
            "AND t.name IN :tags " +
            "GROUP BY i " +
            "HAVING COUNT(t.name) = :tagCount")
    Page<InsertionForRent> searchWithTags(@Param("point") Point point,
                                          @Param("distance") Double distance,
                                          @Param("tags") Set<String> tags,
                                          @Param("tagCount") int tagCount,
                                          @Param("minSize") Double minSize,
                                          @Param("minNumberOfRooms") Integer minNumberOfRooms,
                                          @Param("maxFloor") Integer maxFloor,
                                          @Param("hasElevator") Boolean hasElevator,
                                          @Param("maxRent") Double maxRent,
                                          Pageable pageable);

}
