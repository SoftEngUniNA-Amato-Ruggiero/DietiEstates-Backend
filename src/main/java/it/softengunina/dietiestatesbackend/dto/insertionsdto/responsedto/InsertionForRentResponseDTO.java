package it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto;

import it.softengunina.dietiestatesbackend.model.insertions.InsertionWithRent;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InsertionForRentResponseDTO extends BaseInsertionResponseDTO implements InsertionWithRentResponseDTO {
    private Double rent;

    public InsertionForRentResponseDTO(InsertionWithRent insertion) {
        super(insertion);
        this.rent = insertion.getRent();
    }
}