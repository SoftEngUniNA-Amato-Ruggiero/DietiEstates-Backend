package it.softengunina.dietiestatesbackend.dto.insertionsdto;

import it.softengunina.dietiestatesbackend.model.insertions.InsertionWithRent;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InsertionWithRentDTO extends BaseInsertionDTO {
    @NotNull
    private Double rent;

    public InsertionWithRentDTO(InsertionWithRent insertion) {
        super(insertion);
        this.rent = insertion.getRent();
    }
}
