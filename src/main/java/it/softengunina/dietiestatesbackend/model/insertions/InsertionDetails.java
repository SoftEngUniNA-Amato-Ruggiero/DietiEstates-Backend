package it.softengunina.dietiestatesbackend.model.insertions;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.Set;

/**
 * This class represent the details of a real estate insertion.
 * It includes attributes such as floor, presence of elevator, and tags.
 * It is marked as @Embeddable to indicate that it can be embedded in other entities.
 */
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
