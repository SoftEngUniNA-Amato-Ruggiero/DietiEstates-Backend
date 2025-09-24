package it.softengunina.dietiestatesbackend.dto.insertionsdto;

import it.softengunina.dietiestatesbackend.model.insertions.InsertionDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.geojson.FeatureCollection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsertionRequest {
    private FeatureCollection address;
    private InsertionDetails details;
    private Double price;
    private Double rent;
}
