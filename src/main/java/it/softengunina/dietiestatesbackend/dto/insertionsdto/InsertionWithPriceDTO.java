package it.softengunina.dietiestatesbackend.dto.insertionsdto;

import it.softengunina.dietiestatesbackend.model.insertions.InsertionWithPrice;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InsertionWithPriceDTO extends BaseInsertionDTO {
    @NotNull
    private Double price;

    public InsertionWithPriceDTO(InsertionWithPrice insertion) {
        super(insertion);
        this.price = insertion.getPrice();
    }
}
