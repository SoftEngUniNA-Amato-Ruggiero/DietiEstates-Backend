package it.softengunina.dietiestatesbackend.repository;

import it.softengunina.dietiestatesbackend.model.insertions.Insertion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsertionRepository extends JpaRepository<Insertion, Long> {
}
