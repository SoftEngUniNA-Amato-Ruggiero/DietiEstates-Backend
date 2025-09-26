package it.softengunina.dietiestatesbackend.repository.insertionsrepository;

import it.softengunina.dietiestatesbackend.model.insertions.InsertionForRent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InsertionForRentRepository extends BaseInsertionRepository<InsertionForRent> {
    Page<InsertionForRent> findByRent(Double rent, Pageable pageable);
    Page<InsertionForRent> findByRentLessThanEqual(Double rent, Pageable pageable);
    Page<InsertionForRent> findByRentGreaterThanEqual(Double rent, Pageable pageable);
}
