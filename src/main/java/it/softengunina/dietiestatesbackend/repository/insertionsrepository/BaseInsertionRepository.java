package it.softengunina.dietiestatesbackend.repository.insertionsrepository;

import it.softengunina.dietiestatesbackend.model.Address;
import it.softengunina.dietiestatesbackend.model.insertions.BaseInsertion;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface BaseInsertionRepository<T extends BaseInsertion> extends JpaRepository<T, Long> {
    Page<T> findByUploader(RealEstateAgent uploader, Pageable pageable);

    Page<T> findByAddress(Address address, Pageable pageable);
    Page<T> findByAddress_Street(String street, Pageable pageable);
    Page<T> findByAddress_Suburb(String suburb, Pageable pageable);
    Page<T> findByAddress_City(String city, Pageable pageable);
    Page<T> findByAddress_County(String county, Pageable pageable);
    Page<T> findByAddress_State(String state, Pageable pageable);
    Page<T> findByAddress_Country(String country, Pageable pageable);

    @Transactional
    @Query("SELECT i FROM #{#entityName} i WHERE function('ST_DWithin', i.address.location, :point, :distance) = true")
    Page<T> findByLocationNear(@Param("point") Point point, @Param("distance") double distance, Pageable pageable);

    @Transactional
    @Query("SELECT i FROM #{#entityName} i " +
            "WHERE function('ST_DWithin', i.address.location, :point, :distance) = true " +
            "AND (:minSize IS NULL OR i.details.size >= :minSize) " +
            "AND (:minNumberOfRooms IS NULL OR i.details.numberOfRooms >= :minNumberOfRooms) " +
            "AND (:maxFloor IS NULL OR i.details.floor <= :maxFloor) " +
            "AND (:hasElevator IS NULL OR i.details.hasElevator = :hasElevator)")
    Page<T> search_without_tags(@Param("point") Point point,
                               @Param("distance") Double distance,
                               @Param("minSize") Integer minSize,
                               @Param("minNumberOfRooms") Integer minNumberOfRooms,
                               @Param("maxFloor") Integer maxFloor,
                               @Param("hasElevator") Boolean hasElevator,
                               Pageable pageable);

    @Transactional
    @Query("SELECT i FROM #{#entityName} i JOIN i.tags t " +
            "WHERE function('ST_DWithin', i.address.location, :point, :distance) = true " +
            "AND (:minSize IS NULL OR i.details.size >= :minSize) " +
            "AND (:minNumberOfRooms IS NULL OR i.details.numberOfRooms >= :minNumberOfRooms) " +
            "AND (:maxFloor IS NULL OR i.details.floor <= :maxFloor) " +
            "AND (:hasElevator IS NULL OR i.details.hasElevator = :hasElevator) " +
            "AND t.name IN :tags " +
            "GROUP BY i " +
            "HAVING COUNT(t.name) = :tagCount")
    Page<T> search_with_tags(@Param("point") Point point,
                            @Param("distance") Double distance,
                            @Param("tags") Set<String> tags,
                            @Param("tagCount") int tagCount,
                            @Param("minSize") Integer minSize,
                            @Param("minNumberOfRooms") Integer minNumberOfRooms,
                            @Param("maxFloor") Integer maxFloor,
                            @Param("hasElevator") Boolean hasElevator,
                            Pageable pageable);
}