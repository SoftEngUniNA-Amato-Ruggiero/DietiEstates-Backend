package it.softengunina.dietiestatesbackend.model.insertions;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class InsertionDetails {
    private Short floor;
    private boolean hasElevator;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "insertion_tags",
            joinColumns = @JoinColumn(name = "tags")
    )
    @Column(name = "tag")
    @EqualsAndHashCode.Exclude
    private Set<String> tags;
}
