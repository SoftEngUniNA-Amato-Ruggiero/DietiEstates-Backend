package it.softengunina.dietiestatesbackend.model.insertions;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class InsertionDetails {
    private Double size;
    private Integer numberOfRooms;
    private Integer floor;
    private Boolean hasElevator;
}
