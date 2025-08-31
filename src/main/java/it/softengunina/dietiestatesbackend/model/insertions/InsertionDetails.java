package it.softengunina.dietiestatesbackend.model.insertions;

import jakarta.persistence.Embeddable;
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
    private Set<String> tags;
}
