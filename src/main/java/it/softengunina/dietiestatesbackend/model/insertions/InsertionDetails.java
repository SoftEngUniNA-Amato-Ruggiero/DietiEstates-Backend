package it.softengunina.dietiestatesbackend.model.insertions;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Set;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InsertionDetails {
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "insertion_tags",
            joinColumns = @JoinColumn(name = "insertion_id")
    )
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @Column(name = "tag")
    private Set<String> tags;

    @Column(name = "description", columnDefinition = "TEXT")
    @Lob
    private String description;
}
