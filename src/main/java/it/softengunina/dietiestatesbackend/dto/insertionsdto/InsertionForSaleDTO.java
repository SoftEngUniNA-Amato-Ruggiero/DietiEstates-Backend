package it.softengunina.dietiestatesbackend.dto.insertionsdto;

import it.softengunina.dietiestatesbackend.model.insertions.InsertionForSale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InsertionForSaleDTO extends InsertionDTO {
    public InsertionForSaleDTO(InsertionForSale insertion) {
        super(insertion);
    }
}
