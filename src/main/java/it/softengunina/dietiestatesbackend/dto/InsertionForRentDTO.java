package it.softengunina.dietiestatesbackend.dto;

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
    private Double rent;

    public InsertionForRentDTO(InsertionForRent insertion) {
        super(insertion);
        this.rent = insertion.getRent();
    }
}
