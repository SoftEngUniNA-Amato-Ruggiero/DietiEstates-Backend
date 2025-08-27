package it.softengunina.dietiestatesbackend.factory;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionWithPriceDTO;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionWithPrice;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InsertionWithPriceDTOFactory implements InsertionDTOFactory {
    private InsertionWithPrice insertion;

    public InsertionWithPriceDTO build() {
        return new InsertionWithPriceDTO(this.insertion);
    }
}
