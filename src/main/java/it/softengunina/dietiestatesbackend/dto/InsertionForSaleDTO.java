package it.softengunina.dietiestatesbackend.dto;

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
    private Double price;

    public InsertionForSaleDTO(InsertionForSale insertion) {
        super(insertion);
        this.price = insertion.getPrice();
    }
}
