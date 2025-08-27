package it.softengunina.dietiestatesbackend.dto.insertionsdto;

import it.softengunina.dietiestatesbackend.model.insertions.BaseInsertion;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionForRent;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionForSale;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InsertionDTOFactory {
    public static InsertionDTO createInsertionDTO(BaseInsertion insertion) {
        return switch (insertion) {
            case InsertionForSale i -> createInsertionDTO(i);
            case InsertionForRent i -> createInsertionDTO(i);
            default -> new InsertionDTO(insertion);
        };
    }

    public static InsertionDTO createInsertionDTO(InsertionForSale i) {
        return new InsertionWithPriceDTO(i);
    }

    public static InsertionDTO createInsertionDTO(InsertionForRent i) {
        return new InsertionWithRentDTO(i);
    }
}