package it.softengunina.dietiestatesbackend.repository;

import it.softengunina.dietiestatesbackend.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
}
