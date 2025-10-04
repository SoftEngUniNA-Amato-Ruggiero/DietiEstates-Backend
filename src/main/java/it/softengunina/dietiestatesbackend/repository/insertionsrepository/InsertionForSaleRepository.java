package it.softengunina.dietiestatesbackend.repository.insertionsrepository;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionSearchResultDTO;
import it.softengunina.dietiestatesbackend.dto.searchdto.SearchRequestForSaleDTO;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionForSale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface InsertionForSaleRepository extends BaseInsertionRepository<InsertionForSale> {
    Page<InsertionForSale> findByPrice(Double price, Pageable pageable);
    Page<InsertionForSale> findByPriceLessThanEqual(Double price, Pageable pageable);
    Page<InsertionForSale> findByPriceGreaterThanEqual(Double price, Pageable pageable);

    @Transactional
    @Query("SELECT new it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionSearchResultDTO(i.address.location, i.id) FROM InsertionForSale i " +
            "LEFT JOIN i.tags t ON (:tagCount > 0 AND t.name in :tags) " +
            "WHERE function('ST_DWithin', i.address.location, function('ST_SetSRID', function('ST_MakePoint', :#{#req.lng}, :#{#req.lat}), 4326), :#{#req.distance}) = true " +
            "AND (:#{#req.maxPrice} IS NULL OR i.price <= :#{#req.maxPrice}) " +
            "AND (:#{#req.minSize} IS NULL OR i.details.size >= :#{#req.minSize}) " +
            "AND (:#{#req.minNumberOfRooms} IS NULL OR i.details.numberOfRooms >= :#{#req.minNumberOfRooms}) " +
            "AND (:#{#req.maxFloor} IS NULL OR i.details.floor <= :#{#req.maxFloor}) " +
            "AND (:#{#req.hasElevator} IS NULL OR i.details.hasElevator = :#{#req.hasElevator}) " +
            "GROUP BY i.id, i.address.location " +
            "HAVING :tagCount = 0 OR COUNT(t) = :tagCount")
    Page<InsertionSearchResultDTO> search(@Param("req") SearchRequestForSaleDTO req, Set<String> tags, Integer tagCount, Pageable pageable);
}
