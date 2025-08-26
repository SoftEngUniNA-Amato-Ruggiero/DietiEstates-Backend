package it.softengunina.dietiestatesbackend.controller.insertionscontroller;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionDTO;
import it.softengunina.dietiestatesbackend.model.insertions.Insertion;
import it.softengunina.dietiestatesbackend.repository.insertionsrepository.InsertionRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/insertions")
public class InsertionController {
    private final InsertionRepository<Insertion> insertionRepository;

    public InsertionController(InsertionRepository<Insertion> insertionRepository) {
        this.insertionRepository = insertionRepository;
    }

    @GetMapping("/{id}")
    public InsertionDTO getInsertionById(@PathVariable Long id) {
        Insertion insertion = insertionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insertion not found"));
        return new InsertionDTO(insertion);
    }
}
