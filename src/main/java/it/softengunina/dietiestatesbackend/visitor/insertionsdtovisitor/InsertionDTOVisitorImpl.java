package it.softengunina.dietiestatesbackend.visitor.insertionsdtovisitor;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionWithPriceDTO;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionWithRentDTO;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionWithPrice;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionWithRent;
import org.springframework.stereotype.Component;

@Component
public class InsertionDTOVisitorImpl implements InsertionDTOVisitor {

    @Override
    public InsertionWithRentDTO visit(InsertionWithRent i) {
        return new InsertionWithRentDTO(i);
    }

    @Override
    public InsertionWithPriceDTO visit(InsertionWithPrice i) {
        return new  InsertionWithPriceDTO(i);
    }
}
