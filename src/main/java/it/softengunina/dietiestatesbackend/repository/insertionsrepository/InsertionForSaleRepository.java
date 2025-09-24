package it.softengunina.dietiestatesbackend.repository.insertionsrepository;

import it.softengunina.dietiestatesbackend.model.insertions.InsertionForSale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InsertionForSaleRepository extends InsertionRepository<InsertionForSale> {
    Page<InsertionForSale> findByPrice(Double price, Pageable pageable);
    Page<InsertionForSale> findByPriceLessThanEqual(Double price, Pageable pageable);
    Page<InsertionForSale> findByPriceGreaterThanEqual(Double price, Pageable pageable);
}
