package it.softengunina.userservice.repository;

import it.softengunina.userservice.model.insertions.Insertion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsertionRepository extends JpaRepository<Insertion, Long> {
}
