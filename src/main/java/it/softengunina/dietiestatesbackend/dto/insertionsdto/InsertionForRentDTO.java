package it.softengunina.dietiestatesbackend.dto.insertionsdto;

import it.softengunina.dietiestatesbackend.model.insertions.InsertionForRent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InsertionForRentDTO extends InsertionDTO {
    public InsertionForRentDTO(InsertionForRent insertion) {
        super(insertion);
    }
}
