package it.softengunina.dietiestatesbackend.factory.insertionfactory;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.requestdto.BaseInsertionRequestDTO;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionDetails;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class InsertionDetailsFactory {
    public static InsertionDetails create(@NonNull BaseInsertionRequestDTO req) {
        return InsertionDetails.builder()
                .size(req.getSize())
                .numberOfRooms(req.getNumberOfRooms())
                .floor(req.getFloor())
                .hasElevator(req.getHasElevator())
                .build();
    }
}
