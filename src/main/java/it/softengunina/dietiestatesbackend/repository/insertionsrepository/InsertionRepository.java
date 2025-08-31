package it.softengunina.dietiestatesbackend.repository.insertionsrepository;

import it.softengunina.dietiestatesbackend.model.insertions.BaseInsertion;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsertionRepository<T extends BaseInsertion> extends JpaRepository<T, Long> {
    Page<T> findByUploader(RealEstateAgent uploader, Pageable pageable);
    Page<T> findByAddress_City(String city, Pageable pageable);
}
