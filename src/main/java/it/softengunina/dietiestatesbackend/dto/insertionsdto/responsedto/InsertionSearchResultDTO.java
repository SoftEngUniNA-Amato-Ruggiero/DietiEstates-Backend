package it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto;

import lombok.*;
import org.locationtech.jts.geom.Point;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class InsertionSearchResultDTO {
    private Point location;
    private Long id;
}
