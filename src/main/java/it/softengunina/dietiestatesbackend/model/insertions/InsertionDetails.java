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
@ToString
@EqualsAndHashCode
public class InsertionDetails {
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "insertion_tags",
            joinColumns = @JoinColumn(name = "tags")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name = "tag")
    @EqualsAndHashCode.Exclude
    private Set<String> tags;

    @Column(name = "description", columnDefinition = "TEXT")
    @Lob
    @EqualsAndHashCode.Exclude
    private String description;
}
