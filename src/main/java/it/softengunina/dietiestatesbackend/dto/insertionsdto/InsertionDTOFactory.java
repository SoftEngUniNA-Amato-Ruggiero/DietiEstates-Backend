package it.softengunina.dietiestatesbackend.dto.insertionsdto;

import it.softengunina.dietiestatesbackend.model.insertions.Insertion;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionForRent;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionForSale;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InsertionDTOFactory {
    public static InsertionDTO createInsertionDTO(Insertion insertion) {
        return switch (insertion) {
            case InsertionForSale i -> createInsertionDTO(i);
            case InsertionForRent i -> createInsertionDTO(i);
            default -> new InsertionDTO(insertion);
        };
    }

    public static InsertionDTO createInsertionDTO(InsertionForSale i) {
        return new InsertionForSaleDTO(i);
    }

    public static InsertionDTO createInsertionDTO(InsertionForRent i) {
        return new InsertionForRentDTO(i);
    }
}