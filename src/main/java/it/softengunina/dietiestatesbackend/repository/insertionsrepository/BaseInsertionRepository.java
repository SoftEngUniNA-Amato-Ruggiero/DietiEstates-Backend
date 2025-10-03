package it.softengunina.dietiestatesbackend.repository.insertionsrepository;

import it.softengunina.dietiestatesbackend.dto.searchdto.SearchRequestDTO;
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
    @Query("SELECT i FROM #{#entityName} i LEFT JOIN i.tags t ON (:tagCount > 0 AND t.name in :tags) " +
            "WHERE function('ST_DWithin', i.address.location, function('ST_SetSRID', function('ST_MakePoint', :#{#req.lng}, :#{#req.lat}), 4326), :#{#req.distance}) = true " +
            "AND (:#{#req.minSize} IS NULL OR i.details.size >= :#{#req.minSize}) " +
            "AND (:#{#req.minNumberOfRooms} IS NULL OR i.details.numberOfRooms >= :#{#req.minNumberOfRooms}) " +
            "AND (:#{#req.maxFloor} IS NULL OR i.details.floor <= :#{#req.maxFloor}) " +
            "AND (:#{#req.hasElevator} IS NULL OR i.details.hasElevator = :#{#req.hasElevator}) " +
            "GROUP BY i " +
            "HAVING :tagCount = 0 OR COUNT(t) = :tagCount")
    Page<T> search(@Param("req")SearchRequestDTO req, Set<String> tags, Integer tagCount, Pageable pageable);
}