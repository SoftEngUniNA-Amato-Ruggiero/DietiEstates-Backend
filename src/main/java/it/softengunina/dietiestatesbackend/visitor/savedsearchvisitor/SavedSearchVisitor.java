package it.softengunina.dietiestatesbackend.visitor.savedsearchvisitor;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionSearchResultDTO;
import it.softengunina.dietiestatesbackend.model.savedsearches.SavedSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SavedSearchVisitor {
    Page<InsertionSearchResultDTO> visit(SavedSearch savedSearch, Pageable pageable);
}
