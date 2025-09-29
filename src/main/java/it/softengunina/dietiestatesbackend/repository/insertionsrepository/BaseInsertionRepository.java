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

import java.util.List;
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


    @Query("SELECT i FROM #{#entityName} i JOIN i.tags t WHERE t.name IN :tags GROUP BY i HAVING COUNT(t.name) = :tagCount")
    Page<T> findByAllTagsPresent(@Param("tags") Set<String> tags, @Param("tagCount") int tagCount, Pageable pageable);

    @Query(value =
            "SELECT i.* " +
            "FROM tags t " +
            "JOIN insertion_tags it ON t.id = it.tag_id " +
            "JOIN insertions i ON it.insertion_id = i.id " +
            "WHERE t.name IN :tags " +
            "GROUP BY i.id " +
            "HAVING COUNT(t.name) = :tagCount",
            nativeQuery = true)
    Page<T> findByAllTagsPresent_Native(@Param("tags") Set<String> tags, @Param("tagCount") int tagCount, Pageable pageable);


    @Transactional
    @Query("SELECT i FROM #{#entityName} i WHERE function('ST_DWithin', i.address.location, :point, :distance) = true")
    Page<T> findByLocationNear(@Param("point") Point point, @Param("distance") double distance, Pageable pageable);

    @Transactional
    @Query(value =
            "SELECT i.*, a.* " +
            "FROM addresses a " +
            "JOIN insertions i ON i.address_id = a.id " +
            "WHERE ST_DWithin(a.location, :point, :distance)",
            nativeQuery = true)
    Page<T> findByLocationNear_Native(@Param("point") Point point, @Param("distance") double distance, Pageable pageable);


    @Transactional
    @Query("SELECT i FROM #{#entityName} i JOIN i.tags t " +
            "WHERE function('ST_DWithin', i.address.location, :point, :distance) = true " +
            "AND t.name IN :tags " +
            "GROUP BY i " +
            "HAVING COUNT(DISTINCT t.name) = :tagCount")
    Page<T> findByLocationNearAndAllTagsPresent(@Param("point") Point point,
                                                @Param("distance") double distance,
                                                @Param("tags") List<String> tags,
                                                @Param("tagCount") int tagCount,
                                                Pageable pageable);

    @Transactional
    @Query(value =
            "WITH insertions_in_range AS ( " +
                "SELECT i.id AS insertion_id, a.id AS address_id " +
                "FROM addresses a " +
                "JOIN insertions i ON i.address_id = a.id " +
                "WHERE ST_DWithin(a.location, :point, :distance) " +
            "), " +
            "matched_insertions AS ( " +
                "SELECT i.insertion_id, i.address_id " +
                "FROM insertions_in_range i " +
                "JOIN insertion_tags it ON it.insertion_id = i.insertion_id  " +
                "JOIN tags t ON t.id = it.tag_id  " +
                "WHERE t.name in :tags " +
                "GROUP BY i.insertion_id, i.address_id  " +
                "HAVING count (t.name) = :tagCount " +
            ") " +
            "SELECT i.*, a.* " +
            "FROM matched_insertions mi " +
            "JOIN addresses a ON mi.address_id = a.id " +
            "JOIN insertions i ON mi.insertion_id = i.id",
            nativeQuery = true)
    Page<T> findByLocationNearAndAllTagsPresent_Native(@Param("point") Point point,
                                                @Param("distance") double distance,
                                                @Param("tags") List<String> tags,
                                                @Param("tagCount") int tagCount,
                                                Pageable pageable);
}