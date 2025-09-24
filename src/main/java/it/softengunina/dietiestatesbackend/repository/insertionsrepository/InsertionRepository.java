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

public interface InsertionRepository<T extends BaseInsertion> extends JpaRepository<T, Long> {
    Page<T> findByUploader(RealEstateAgent uploader, Pageable pageable);

    Page<T> findByAddress(Address address, Pageable pageable);
    Page<T> findByAddress_Street(String street, Pageable pageable);
    Page<T> findByAddress_Suburb(String suburb, Pageable pageable);
    Page<T> findByAddress_City(String city, Pageable pageable);
    Page<T> findByAddress_County(String county, Pageable pageable);
    Page<T> findByAddress_State(String state, Pageable pageable);
    Page<T> findByAddress_Country(String country, Pageable pageable);

    Page<T> findByDetails_TagsContaining(String tag, Pageable pageable);
    Page<T> findByDetails_DescriptionContaining(String description, Pageable pageable);

    @Query("SELECT i FROM BaseInsertion i WHERE function('ST_DWithin', i.address.location, :point, :distance) = true")
    Page<T> findByLocationNear(@Param("point") Point point, @Param("distance") double distance, Pageable pageable);
}