package it.softengunina.dietiestatesbackend.visitor.savedsearchvisitor;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionSearchResultDTO;
import it.softengunina.dietiestatesbackend.dto.searchdto.SearchRequestDTO;
import it.softengunina.dietiestatesbackend.model.insertions.BaseInsertion;
import it.softengunina.dietiestatesbackend.model.savedsearches.SavedSearch;
import it.softengunina.dietiestatesbackend.repository.insertionsrepository.BaseInsertionRepository;
import it.softengunina.dietiestatesbackend.repository.insertionsrepository.InsertionForRentRepository;
import it.softengunina.dietiestatesbackend.repository.insertionsrepository.InsertionForSaleRepository;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class SavedSearchVisitorImpl implements SavedSearchVisitor {
    BaseInsertionRepository<BaseInsertion> baseInsertionRepository;
    InsertionForSaleRepository insertionForSaleRepository;
    InsertionForRentRepository insertionForRentRepository;

    public SavedSearchVisitorImpl(BaseInsertionRepository<BaseInsertion> baseInsertionRepository,
                                  InsertionForSaleRepository insertionForSaleRepository,
                                  InsertionForRentRepository insertionForRentRepository) {
        this.baseInsertionRepository = baseInsertionRepository;
        this.insertionForSaleRepository = insertionForSaleRepository;
        this.insertionForRentRepository = insertionForRentRepository;
    }

    public Page<InsertionSearchResultDTO> visit(SavedSearch savedSearch, Pageable pageable) {
        SearchRequestDTO dto = SearchRequestDTO.builder()
                .lng(((Point) savedSearch.getGeometry()).getX())
                .lat(((Point) savedSearch.getGeometry()).getY())
                .distance(savedSearch.getDistance())
                .minSize(savedSearch.getMinSize())
                .minNumberOfRooms(savedSearch.getMinNumberOfRooms())
                .maxFloor(savedSearch.getMaxFloor())
                .hasElevator(savedSearch.getHasElevator())
                .build();
        return baseInsertionRepository.search(dto, savedSearch.getTags(), savedSearch.getTags().size(), pageable);
    }
}
