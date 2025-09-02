package it.softengunina.dietiestatesbackend.visitor.insertionsdtovisitor;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionWithPriceDTO;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionWithRentDTO;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionForRent;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionForSale;
import org.springframework.stereotype.Component;

@Component
public class InsertionDTOVisitorImpl implements InsertionDTOVisitor {

    @Override
    public InsertionWithRentDTO visit(InsertionForRent i) {
        return new InsertionWithRentDTO(i);
    }

    @Override
    public InsertionWithPriceDTO visit(InsertionForSale i) {
        return new  InsertionWithPriceDTO(i);
    }
}
