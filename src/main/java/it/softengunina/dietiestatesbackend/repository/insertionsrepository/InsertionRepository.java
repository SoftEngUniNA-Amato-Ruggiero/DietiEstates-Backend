package it.softengunina.dietiestatesbackend.repository.insertionsrepository;

import it.softengunina.dietiestatesbackend.model.insertions.Insertion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InsertionRepository<T extends Insertion> extends JpaRepository<T, Long> {
    @Query("SELECT i FROM #{#entityName} i WHERE i.address.city = :city")
    Page<T> findByCity(String city, Pageable pageable);

    @Query("SELECT i FROM #{#entityName} i WHERE CONCAT(i.address.street, ' ', i.address.houseNumber, ', ', i.address.city, ' ', i.address.postalCode) LIKE %:address%")
    Page<T> findByAddress(String address, Pageable pageable);
}
