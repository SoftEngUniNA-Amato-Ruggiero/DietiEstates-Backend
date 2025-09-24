package it.softengunina.dietiestatesbackend.dto;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateAgencyResponseDTO {
    @NotNull
    private Long id;

    @NotBlank
    private String iban;

    @NotBlank
    private String name;

    public RealEstateAgencyResponseDTO(RealEstateAgency agency) {
        this.id = agency.getId();
        this.iban = agency.getIban();
        this.name = agency.getName();
    }
}
