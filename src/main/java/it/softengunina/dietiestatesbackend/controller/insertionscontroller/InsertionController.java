package it.softengunina.dietiestatesbackend.controller.insertionscontroller;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionDTO;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionDTOFactory;
import it.softengunina.dietiestatesbackend.model.insertions.BaseInsertion;
import it.softengunina.dietiestatesbackend.repository.insertionsrepository.InsertionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/insertions")
public class InsertionController {
    private final InsertionRepository<BaseInsertion> insertionRepository;

    public InsertionController(InsertionRepository<BaseInsertion> insertionRepository) {
        this.insertionRepository = insertionRepository;
    }

    @GetMapping
    public Page<InsertionDTO> getInsertions(Pageable pageable) {
        return insertionRepository.findAll(pageable).map(InsertionDTOFactory::createInsertionDTO);
    }

    @GetMapping("/{id}")
    public InsertionDTO getInsertionById(@PathVariable Long id) {
        BaseInsertion insertion = insertionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insertion not found"));
        return InsertionDTOFactory.createInsertionDTO(insertion);
    }
}
