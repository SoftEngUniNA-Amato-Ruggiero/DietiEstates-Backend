package it.softengunina.dietiestatesbackend.dto.insertionsdto.requestdto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.geojson.FeatureCollection;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class BaseInsertionRequestDTO {
    @NotNull
    private String description;
    @NotNull
    private Set<String> tags;
    @NonNull
    private FeatureCollection address;
}
