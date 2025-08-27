package it.softengunina.dietiestatesbackend.dto.insertionsdto;

import it.softengunina.dietiestatesbackend.model.insertions.InsertionWithRent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InsertionWithRentDTO extends BaseInsertionDTO {
    private Double rent;

    public InsertionWithRentDTO(InsertionWithRent insertion) {
        super(insertion);
        this.rent = insertion.getRent();
    }
}
