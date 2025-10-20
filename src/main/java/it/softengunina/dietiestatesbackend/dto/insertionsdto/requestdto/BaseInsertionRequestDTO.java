package it.softengunina.dietiestatesbackend.dto.insertionsdto.requestdto;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.geojson.FeatureCollection;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode
@ToString
public abstract class BaseInsertionRequestDTO {
    @NotNull
    private String description;
    @NotNull
    private Set<String> tags;
    @NotNull
    private FeatureCollection address;

    @Positive
    private Double size;
    @Positive
    private Integer numberOfRooms;

    private Integer floor;
    private Boolean hasElevator;
}
