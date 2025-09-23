package it.softengunina.dietiestatesbackend.dto.insertionsdto;

import it.softengunina.dietiestatesbackend.dto.RealEstateAgencyDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserDTO;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionDetails;
import lombok.Data;
import org.geojson.FeatureCollection;

@Data
public class InsertionRequest {
    private Long id;
    private FeatureCollection address;
    private InsertionDetails details;
    private UserDTO uploader;
    private RealEstateAgencyDTO agency;
    private Long price;
    private Long rent;
}
