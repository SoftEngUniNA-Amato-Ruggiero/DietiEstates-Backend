package it.softengunina.dietiestatesbackendservice.repository;

import it.softengunina.dietiestatesbackendservice.model.insertions.Insertion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsertionRepository extends JpaRepository<Insertion, Long> {
}
