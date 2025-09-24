package it.softengunina.dietiestatesbackend.dto;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateAgencyRequestDTO {
    @NotBlank
    private String iban;

    @NotBlank
    private String name;

    public RealEstateAgencyRequestDTO(RealEstateAgency agency) {
        this.iban = agency.getIban();
        this.name = agency.getName();
    }
}
