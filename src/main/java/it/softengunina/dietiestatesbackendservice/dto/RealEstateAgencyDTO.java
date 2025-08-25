package it.softengunina.dietiestatesbackendservice.dto;

import it.softengunina.dietiestatesbackendservice.model.RealEstateAgency;
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
