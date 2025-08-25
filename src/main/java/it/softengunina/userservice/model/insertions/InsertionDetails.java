package it.softengunina.userservice.model.insertions;

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
    private short floor;
    private boolean hasElevator;
    private Set<String> tags;

}
