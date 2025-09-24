package it.softengunina.dietiestatesbackend.repository;

import it.softengunina.dietiestatesbackend.dto.RealEstateAgencyResponseDTO;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RealEstateAgencyRepository extends JpaRepository<RealEstateAgency,Long> {
    Page<RealEstateAgencyResponseDTO> findAllBy(Pageable pageable);

    Optional<RealEstateAgencyResponseDTO> findDTOById(Long id);

    Optional<RealEstateAgency> findByName(String name);
    Optional<RealEstateAgencyResponseDTO> findDTOByName(String name);

    Optional<RealEstateAgency> findByIban(String iban);
    Optional<RealEstateAgencyResponseDTO> findDTOByIban(String iban);
}
