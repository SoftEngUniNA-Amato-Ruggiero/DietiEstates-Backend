package it.softengunina.dietiestatesbackend.factory;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionWithRentDTO;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionWithRent;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InsertionWithRentDTOFactory implements InsertionDTOFactory {
    private InsertionWithRent insertion;

    public InsertionWithRentDTO build() {
        return new InsertionWithRentDTO(this.insertion);
    }
}
