package it.softengunina.dietiestatesbackend.dto;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RealEstateAgencyDTO {
    @NotBlank
    private String iban;

    @NotBlank
    private String name;

    public RealEstateAgencyDTO (RealEstateAgency agency) {
        this.iban = agency.getIban();
        this.name = agency.getName();
    }
}
