package it.softengunina.dietiestatesbackend.dto.insertionsdto;

import it.softengunina.dietiestatesbackend.model.insertions.InsertionWithPrice;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InsertionWithPriceDTO extends BaseInsertionDTO {
    @NotNull
    private Double price;

    public InsertionWithPriceDTO(InsertionWithPrice insertion) {
        super(insertion);
        this.price = insertion.getPrice();
    }
}
