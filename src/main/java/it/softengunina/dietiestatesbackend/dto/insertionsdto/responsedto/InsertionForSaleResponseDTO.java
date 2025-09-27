package it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto;

import it.softengunina.dietiestatesbackend.model.insertions.InsertionWithPrice;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InsertionForSaleResponseDTO extends BaseInsertionResponseDTO implements InsertionWithPriceResponseDTO {
    private Double price;

    public InsertionForSaleResponseDTO(InsertionWithPrice insertion) {
        super(insertion);
        this.price = insertion.getPrice();
    }
}
