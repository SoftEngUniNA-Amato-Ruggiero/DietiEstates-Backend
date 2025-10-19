package it.softengunina.dietiestatesbackend.listeners;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionSearchResultDTO;
import it.softengunina.dietiestatesbackend.model.insertions.Tag;
import it.softengunina.dietiestatesbackend.model.insertions.Insertion;
import it.softengunina.dietiestatesbackend.repository.TagRepository;
import it.softengunina.dietiestatesbackend.services.NotificationsServiceImpl;
import it.softengunina.dietiestatesbackend.visitor.insertionsdtovisitor.InsertionDTOVisitorImpl;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InsertionsListener {

    TagRepository tagRepository;
    InsertionDTOVisitorImpl dtoVisitor;
    NotificationsServiceImpl notificationsService;

    public InsertionsListener(@Lazy TagRepository tagRepository,
                              @Lazy InsertionDTOVisitorImpl dtoVisitor,
                              @Lazy NotificationsServiceImpl notificationsService) {
        this.tagRepository = tagRepository;
        this.dtoVisitor = dtoVisitor;
        this.notificationsService = notificationsService;
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
        String message = buildMessage(insertion);
        Map<String, String> attributes = Map.of(
                "type", insertion.getClass().getSimpleName(),
                "city", insertion.getAddress().getCity()
        );
        notificationsService.publishMessageToTopic(message, attributes);
    }

    private String buildMessage(Insertion insertion) {
        return String.valueOf(
                new InsertionSearchResultDTO(insertion.getAddress().getLocation(), insertion.getId())
        );
    }

}
