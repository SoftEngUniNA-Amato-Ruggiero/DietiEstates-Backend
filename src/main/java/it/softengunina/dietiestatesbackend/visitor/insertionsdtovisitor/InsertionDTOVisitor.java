package it.softengunina.dietiestatesbackend.visitor.insertionsdtovisitor;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionDTO;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionForRent;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionForSale;

public interface InsertionDTOVisitor {
    InsertionDTO visit(InsertionForRent i);
    InsertionDTO visit(InsertionForSale i);
}
