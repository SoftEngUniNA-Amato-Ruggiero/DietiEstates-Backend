package it.softengunina.dietiestatesbackend.factory.insertionfactory;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.requestdto.BaseInsertionRequestDTO;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionDetails;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class InsertionDetailsFactory {
    public static InsertionDetails create(BaseInsertionRequestDTO req) {
        return InsertionDetails.builder()
                .size(req.getSize())
                .numberOfRooms(req.getNumberOfRooms())
                .floor(req.getFloor())
                .hasElevator(req.getHasElevator())
                .build();
    }
}
