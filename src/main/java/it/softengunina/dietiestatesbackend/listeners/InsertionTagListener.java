package it.softengunina.dietiestatesbackend.listeners;

import it.softengunina.dietiestatesbackend.model.insertions.Tag;
import it.softengunina.dietiestatesbackend.model.insertions.Insertion;
import it.softengunina.dietiestatesbackend.repository.TagRepository;
import jakarta.persistence.PrePersist;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InsertionTagListener {
    TagRepository tagRepository;

    public InsertionTagListener(@Lazy TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @PrePersist
    public void assignTag(Insertion insertion)  {
        Set<Tag> managedTags = insertion.getTags().stream()
                .map(tag -> tagRepository.findByName(tag.getName())
                        .orElseGet(() -> tagRepository.saveAndFlush(new Tag(tag.getName()))))
                .collect(Collectors.toSet());
        insertion.setTags(managedTags);
    }
}
