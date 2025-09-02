package it.softengunina.dietiestatesbackend.visitor.insertionsdtovisitor;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionDTO;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionWithPrice;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionWithRent;

public interface InsertionDTOVisitor {
    InsertionDTO visit(InsertionWithRent i);
    InsertionDTO visit(InsertionWithPrice i);
}
