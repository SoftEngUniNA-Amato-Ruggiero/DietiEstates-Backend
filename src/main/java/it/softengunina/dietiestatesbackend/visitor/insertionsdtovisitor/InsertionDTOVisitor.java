package it.softengunina.dietiestatesbackend.visitor.insertionsdtovisitor;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionForRentResponseDTO;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionForSaleResponseDTO;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionWithPrice;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionWithRent;

public interface InsertionDTOVisitor {
    InsertionForSaleResponseDTO visit(InsertionWithPrice i);
    InsertionForRentResponseDTO visit(InsertionWithRent i);
}
