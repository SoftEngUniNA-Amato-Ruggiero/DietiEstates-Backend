package it.softengunina.dietiestatesbackend.repository.insertionsrepository;

import it.softengunina.dietiestatesbackend.model.insertions.Insertion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsertionRepository<T extends Insertion> extends JpaRepository<T, Long> {
}
