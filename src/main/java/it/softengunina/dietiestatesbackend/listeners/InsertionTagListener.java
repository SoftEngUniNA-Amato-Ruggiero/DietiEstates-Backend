package it.softengunina.dietiestatesbackend.listeners;

import it.softengunina.dietiestatesbackend.model.insertions.Tag;
import it.softengunina.dietiestatesbackend.model.insertions.Insertion;
import it.softengunina.dietiestatesbackend.repository.TagRepository;
import it.softengunina.dietiestatesbackend.services.NotificationsPublishingService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InsertionTagListener {
    TagRepository tagRepository;
    NotificationsPublishingService notificationsPublishingService;

    public InsertionTagListener(@Lazy TagRepository tagRepository,
                                @Lazy NotificationsPublishingService notificationsPublishingService) {
        this.tagRepository = tagRepository;
        this.notificationsPublishingService = notificationsPublishingService;
    }

    @PrePersist
    public void assignTag(Insertion insertion)  {
        Set<Tag> managedTags = insertion.getTags().stream()
                .map(tag -> tagRepository.findByName(tag.getName())
                        .orElseGet(() -> tagRepository.saveAndFlush(new Tag(tag.getName()))))
                .collect(Collectors.toSet());
        insertion.setTags(managedTags);
    }

    @PostPersist
    public void sendNotification(Insertion insertion) {
        String message = String.format("New insertion added: %s", insertion);
        notificationsPublishingService.sendNotification(message);
    }
}
