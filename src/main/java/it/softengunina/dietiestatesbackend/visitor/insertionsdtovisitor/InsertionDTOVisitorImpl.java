package it.softengunina.dietiestatesbackend.visitor.insertionsdtovisitor;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionForRentResponseDTO;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionForSaleResponseDTO;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionWithPrice;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionWithRent;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class InsertionDTOVisitorImpl implements InsertionDTOVisitor {
    @Override
    public InsertionForSaleResponseDTO visit(@NonNull InsertionWithPrice i) {
        return new InsertionForSaleResponseDTO(i);
    }

    @Override
    public InsertionForRentResponseDTO visit(@NonNull InsertionWithRent i) {
        return new InsertionForRentResponseDTO(i);
    }
}
