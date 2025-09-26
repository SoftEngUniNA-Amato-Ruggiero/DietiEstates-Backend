package it.softengunina.dietiestatesbackend.controller.insertionscontroller;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionResponseDTO;
import it.softengunina.dietiestatesbackend.model.insertions.BaseInsertion;
import it.softengunina.dietiestatesbackend.repository.insertionsrepository.BaseInsertionRepository;
import it.softengunina.dietiestatesbackend.visitor.insertionsdtovisitor.InsertionDTOVisitorImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for handling requests related to all kinds of insertions.
 */
@RestController
@RequestMapping("/insertions")
public class InsertionController {
    private final BaseInsertionRepository<BaseInsertion> insertionRepository;
    private final InsertionDTOVisitorImpl visitor;

    public InsertionController(BaseInsertionRepository<BaseInsertion> insertionRepository, InsertionDTOVisitorImpl visitor) {
        this.insertionRepository = insertionRepository;
        this.visitor = visitor;
    }

    /**
     * Retrieves a paginated list of all insertions.
     *
     * @param pageable Pagination information.
     * @return A page of insertion DTOs.
     */
    @GetMapping
    public Page<InsertionResponseDTO> getInsertions(Pageable pageable) {
        return insertionRepository.findAll(pageable).map(i -> i.accept(visitor));
    }

    /**
     * Retrieves a specific insertion by its ID.
     *
     * @param id The ID of the insertion to retrieve.
     * @return The insertion DTO.
     * @throws ResponseStatusException if the insertion is not found.
     */
    @GetMapping("/{id}")
    public InsertionResponseDTO getInsertionById(@PathVariable Long id) {
        BaseInsertion insertion = insertionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Insertion not found"));
        return insertion.accept(visitor);
    }
}
