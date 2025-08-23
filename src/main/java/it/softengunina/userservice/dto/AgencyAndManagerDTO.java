package it.softengunina.userservice.dto;

import it.softengunina.userservice.model.RealEstateAgency;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AgencyAndManagerDTO {
    @NotNull
    @Valid
    private RealEstateAgency agency;

    @NotNull
    @Valid
    private RealEstateAgency manager;
}